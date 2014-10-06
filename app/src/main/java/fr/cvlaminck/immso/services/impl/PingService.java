package fr.cvlaminck.immso.services.impl;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.data.DatabaseHelper;
import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;
import fr.cvlaminck.immso.data.repositories.MinecraftServerDao;
import fr.cvlaminck.immso.minecraft.MinecraftServer;
import fr.cvlaminck.immso.minecraft.SupportedToolsVersions;
import fr.cvlaminck.immso.minecraft.tools.MinecraftTools;
import fr.cvlaminck.immso.preferences.UserPreferences_;
import fr.cvlaminck.immso.receivers.RefreshReceiver_;
import fr.cvlaminck.immso.utils.TimeFormatter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import rx.util.functions.Func2;

@EIntentService
public class PingService
        extends IntentService {
    private final static String TAG = PingService.class.getSimpleName();
    private final boolean DEBUG = true;

    private final static int AUTOMATIC_REFRESH_REQUEST_CODE = 42;

    private final static int OFFLINE_SERVER_NOTIFICATION_ID = 42;

    /**
     * Number of components bound to this service.
     * Used to know if notifying the end-user is necessary.
     */
    private int bindCount;

    /**
     * Network operations are wake-locked since they can be called by a BroadcastReceiver
     */
    private PowerManager.WakeLock wakeLock;

    private List<MinecraftServerEntity> servers;

    /**
     * Listener called when a new server is added to the list or when a server is removed from the list.
     * This callback is not called when the list is loaded from the database.
     */
    private fr.cvlaminck.immso.services.api.PingService.ServerStatusChangedListener serverStatusChangedListener;

    private Subscription subscription = null;

    @Pref
    protected UserPreferences_ userPreferences;

    @SystemService
    protected PowerManager powerManager;

    @SystemService
    protected AlarmManager alarmManager;

    protected NotificationManagerCompat notificationManager = null;

    @OrmLiteDao(helper = DatabaseHelper.class)
    protected RuntimeExceptionDao<MinecraftServerEntity, Integer> runtimeMinecraftServerDao;

    @OrmLiteDao(helper = DatabaseHelper.class)
    protected MinecraftServerDao minecraftServerDao;

    @Bean
    protected TimeFormatter timeFormatter;

    public PingService() {
        super(PingService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        servers = new ArrayList<>();
        notificationManager = NotificationManagerCompat.from(this);

        //We configure our wake-lock
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PingService.class.getSimpleName());

        //We register our listener on our shared preferences
        final SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        //We configure the AlarmManager if required
        configureAutomaticRefresh(false);
        //We load our observed servers from the sqlite database
        loadServersFromSqlLite();
    }

    @Override
    public IBinder onBind(Intent intent) {
        bindCount++;
        return new PingServiceBinder(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //No code required here since we are using AA @ServiceAction
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bindCount--;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //We unregister our listeners
        final SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @ServiceAction
    protected void refreshAction() {
        checkServers();
    }

    private PendingIntent automaticRefreshPendingIntent(int flags) {
        final Intent intent = new Intent(this, RefreshReceiver_.class);
        return PendingIntent.getBroadcast(this, AUTOMATIC_REFRESH_REQUEST_CODE, intent, flags);
    }

    /**
     * Configure the service to automatically refresh server status on
     * an intervals specified by the user in
     */
    private void configureAutomaticRefresh(boolean force) {
        PendingIntent intent = automaticRefreshPendingIntent(PendingIntent.FLAG_NO_CREATE);
        //If the user has enabled the automatic refresh
        if(userPreferences.refreshAutomaticallyInBackground().get()) {
            if(intent == null || force) {
                intent = automaticRefreshPendingIntent(0);
                final long period = userPreferences.timeInMSBetweenChecks().get();
                if (DEBUG)
                    Log.d(TAG, "Configuring AlarmManager to automatically trigger refresh operation. Period in seconds : " + (period / 1000L));
                alarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        0L,
                        30000, //period,
                        intent
                );
            } else {
                if(DEBUG)
                    Log.d(TAG, "AlarmManager already configured to trigger automatic refresh operation.");
            }
        }
    }

    private void clearAutomaticRefresh() {
        Log.d(TAG, "Clearing AlarmManager. Refresh operation will no more be triggered automatically");
        final PendingIntent intent = automaticRefreshPendingIntent(PendingIntent.FLAG_NO_CREATE);
        if(intent != null)
            alarmManager.cancel(intent);
    }

    private void loadServersFromSqlLite() {
        servers.addAll(runtimeMinecraftServerDao.queryForAll());
    }

    private void checkServers() {
        if (DEBUG)
            Log.d(TAG, "Refreshing server status");
        //If there is already an on-going operation, we abort
        if (subscription != null) {
            if (DEBUG)
                Log.d(TAG, "Ongoing refresh operation. Aborting");
            return;
        }
        //If the list does not contain any server, we just skip to avoid wake-lock
        if(servers.isEmpty()) {
            if(DEBUG)
                Log.d(TAG, "No observed server. Skipping");
            if(serverStatusChangedListener != null)
                serverStatusChangedListener.onAllServerStatusUpdated(servers);
            return;
        }
        //Otherwise, we use RxJava to update the status of all servers
        //First, we need to acquire a wake-lock so we are sure that the device does not goes off
        //when we are pinging all servers
        //TODO : acquire wake-lock when bind to broadcast receiver only ?
        if (DEBUG)
            Log.d(TAG, "Acquiring wake-lock for network operations");
        wakeLock.acquire();


        //We copy the list of server so we are sure that it will not be modified during
        //the update.
        final ArrayList<MinecraftServerEntity> servers = new ArrayList<>(this.servers);
        subscription = Observable.from(servers)
                .map(new Func1<MinecraftServerEntity, MinecraftServerEntity>() {
                    @Override
                    public MinecraftServerEntity call(MinecraftServerEntity server) {
                        //We change the status for PINGING so the user know we are refreshing this server
                        server.setStatus(MinecraftServer.Status.PINGING, true);
                        return server;
                    }
                })
                .map(new Func1<MinecraftServerEntity, MinecraftServerEntity>() {
                    @Override
                    public MinecraftServerEntity call(MinecraftServerEntity server) {
                        if (DEBUG)
                            Log.d(TAG, String.format("Refreshing server status : %s:%d", server.getHost(), server.getPort()));
                        //We look after the version of the tools that must be used for this server.
                        final MinecraftTools tools = SupportedToolsVersions.getInstance().get(server.getToolsVersion());
                        if (DEBUG)
                            Log.d(TAG, String.format("Pinging %s:%d using '%s' implementation of tools", server.getHost(), server.getPort(), tools.getClass().getName()));
                        final MinecraftServer refreshedStatus = tools.pingSender().ping(server.getServer());
                        server.setServer(refreshedStatus, true);
                        return server;
                    }
                })
                .map(new Func1<MinecraftServerEntity, MinecraftServerEntity>() {
                    @Override
                    public MinecraftServerEntity call(MinecraftServerEntity server) {
                        //If the server has gone offline, we update the offline since.
                        if(server.getStatus() == MinecraftServer.Status.OFFLINE && server.getOfflineSince() == 0)
                            server.setOfflineSince(server.getLastUpdateTime());
                        return server;
                    }
                })
                .map(new Func1<MinecraftServerEntity, MinecraftServerEntity>() {
                    @Override
                    public MinecraftServerEntity call(MinecraftServerEntity server) {
                        //We save the result in the sqlite database
                        runtimeMinecraftServerDao.update(server);
                        return server;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.threadPoolForIO())
                .subscribe(observer);
    }

    /**
     * This function notify the user when a server goes offline. The notification is updated
     * as soon as another server is detected offline.
     */
    private void notifyWhenServerIsOffline() {
        //We check if the user has enabled the notification
        if(!userPreferences.notifyWhenServerGoesOffline().get()) {
            Log.d(TAG, "Notifications when a server goes offline are not enabled. Skipping");
            return;
        }
        //TODO
        //We do not want to display a notification if the user is already browsing the interface
        //of our application
        Observable.from(servers)
                .filter(new Func1<MinecraftServerEntity, Boolean>() {
                    @Override
                    public Boolean call(MinecraftServerEntity server) {
                        return server.getStatus() == MinecraftServer.Status.OFFLINE;
                    }
                })
                .toSortedList(new Func2<MinecraftServerEntity, MinecraftServerEntity, Integer>() {
                    @Override
                    public Integer call(MinecraftServerEntity s1, MinecraftServerEntity s2) {
                        return 0; //TODO : Check comparator doc and implements here to have in most recent to older offline server
                    }
                })
                .observeOn(Schedulers.currentThread())
                .subscribeOn(Schedulers.currentThread())
                .subscribe(new Action1<List<MinecraftServerEntity>>() {
                    @Override
                    public void call(List<MinecraftServerEntity> offlineServers) {
                        if(offlineServers.size() > 0) {
                            final Notification notification = buildNotificationForOfflineServers(offlineServers);
                            notificationManager.notify(OFFLINE_SERVER_NOTIFICATION_ID, notification);
                        } else
                            notificationManager.cancel(OFFLINE_SERVER_NOTIFICATION_ID);
                    }
                });
    }

    private Notification buildNotificationForOfflineServers(List<MinecraftServerEntity> offlineServers) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if(offlineServers.size() == 1)
            buildNotificationForOneOfflineServer(builder, offlineServers.get(0));
        else
            buildNotificationForMultipleOfflineServers(builder, offlineServers);
        return builder.build();
    }

    private void buildNotificationForOneOfflineServer(NotificationCompat.Builder builder, MinecraftServerEntity offlineServer) {
        final long offlineTime = offlineServer.getOfflineSince() - offlineServer.getLastUpdateTime();
        builder
                .setSmallIcon(R.drawable.ic_launcher) //TODO : replace with the real icon of the application
                .setContentTitle(offlineServer.getName())
                .setContentText(getString(R.string.offlineServerNotification_oneServer_contentText, timeFormatter.format(offlineTime)));
        //TODO : add the favicon of the server if available. for 1.7+ servers only
    }

    private void buildNotificationForMultipleOfflineServers(NotificationCompat.Builder builder, List<MinecraftServerEntity> offlineServers) {
        final MinecraftServerEntity offlineServer = offlineServers.get(0);
        final long offlineTime = offlineServer.getOfflineSince() - offlineServer.getLastUpdateTime();
        builder
                .setSmallIcon(R.drawable.ic_launcher) //TODO : replace with the real icon of the application
                .setContentTitle(getString(R.string.offlineServerNotification_multipleServers_contentTitle, offlineServers.size()))
                .setContentText(getString(R.string.offlineServerNotification_multipleServers_contentText, offlineServer.getName(),
                        offlineServers.size() - 1, timeFormatter.format(offlineTime)));
        //TODO : add a icon
    }

    /*package*/ List<MinecraftServerEntity> getServers() {
        return servers;
    }

    /*package*/ void addServer(MinecraftServerEntity server) {
        //TODO Check if the server is correctly formatted
        //First, we need to save the new server in our database
        runtimeMinecraftServerDao.create(server);
        //Then, we add this server to the list
        servers.add(server);
        //And we notify the view that the data set has changed
        serverStatusChangedListener.onServerListChanged(servers);
    }

    /*package*/ void registerServerStatusChangedListener(fr.cvlaminck.immso.services.api.PingService.ServerStatusChangedListener listener) {
        serverStatusChangedListener = listener;
    }

    /*package*/ void unregisterServerStatusChangedListener(fr.cvlaminck.immso.services.api.PingService.ServerStatusChangedListener listener) {
        if (listener == serverStatusChangedListener) {
            serverStatusChangedListener = null;
        }
    }

    /*package*/ void refreshServerStatus() {
        checkServers();
    }

    private Observer<MinecraftServerEntity> observer = new Observer<MinecraftServerEntity>() {
        @Override
        public void onCompleted() {
            subscription = null;
            //We release the wake-lock
            if(DEBUG)
                Log.d(TAG, "Releasing wake-lock");
            wakeLock.release();
            if (DEBUG)
                Log.d(TAG, "Refresh completed, notifying registered listener");
            if (serverStatusChangedListener != null)
                serverStatusChangedListener.onAllServerStatusUpdated(servers);
        }

        @Override
        public void onError(Throwable e) {
            //TODO
            e.printStackTrace();
        }

        @Override
        public void onNext(MinecraftServerEntity server) {
            if (DEBUG)
                Log.d(TAG, String.format("Server status refreshed : %s:%d -> %s", server.getHost(), server.getPort(), server.getStatus().name()));
            //We notify the user about server status
            notifyWhenServerIsOffline();
        }
    };

    /**
     * Listener that will reconfigure the automatic refresh after the user has done changes in the
     * application preferences.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.userpreferences_refreshAutomaticallyInBackground_key))){
                if(userPreferences.refreshAutomaticallyInBackground().get()) {
                    configureAutomaticRefresh(false);
                } else {
                    clearAutomaticRefresh();
                }
            } else if (key.equals(getString(R.string.userpreferences_timeInMSBetweenChecks_key))) {
                //We should change the timer period only if the automatic refresh is enabled by the user
                if(userPreferences.refreshAutomaticallyInBackground().get()) {
                    clearAutomaticRefresh();
                    configureAutomaticRefresh(true);
                }
            }
        }

    };


}
