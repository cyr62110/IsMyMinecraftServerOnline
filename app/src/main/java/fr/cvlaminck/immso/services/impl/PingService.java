/**
 * Copyright 2014 Cyril Vlaminck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.cvlaminck.immso.services.impl;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ServiceAction;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.activities.HomeActivity_;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

@EIntentService
public class PingService
        extends IntentService {
    private final static String TAG = PingService.class.getSimpleName();
    private final boolean DEBUG = true;

    /**
     * Refresh period that will be forced if the application is running in DEBUG mode
     * Set to 0 if you want to have the default behavior.
     */
    private final long DEBUG_FIXED_PERIOD = 30000;

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

    @ServiceAction
    protected void onNotificationDismissed(final ArrayList<Integer> serversRequiringNotification) {
        //Offline servers that have seen their notification removed should not
        //appear in the notifications next time the app check all server status since
        //the user already know they are offline.
        Observable.from(servers)
                .filter(new Func1<MinecraftServerEntity, Boolean>() {
                    @Override
                    public Boolean call(MinecraftServerEntity server) {
                        return serversRequiringNotification.contains(server.getId());
                    }
                })
                .map(new Func1<MinecraftServerEntity, MinecraftServerEntity>() {
                    @Override
                    public MinecraftServerEntity call(MinecraftServerEntity server) {
                        server.setHasOfflineStatusBeenSeen(true);
                        return server;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MinecraftServerEntity>() {
                    @Override
                    public void call(MinecraftServerEntity server) {
                        if (DEBUG)
                            Log.d(TAG, String.format("'%s' offline status has been seen by the user. Updating the database", server.getName()));
                        //We update the value in the database
                        runtimeMinecraftServerDao.update(server);
                    }
                });
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
        if (userPreferences.refreshAutomaticallyInBackground().get()) {
            if (intent == null || force) {
                intent = automaticRefreshPendingIntent(0);
                final long period;
                //In DEBUG mode, the period is fixed to 30 second
                if (DEBUG && DEBUG_FIXED_PERIOD > 0)
                    period = DEBUG_FIXED_PERIOD;
                else
                    period = userPreferences.timeInMSBetweenChecks().get();
                if (DEBUG)
                    Log.d(TAG, "Configuring AlarmManager to automatically trigger refresh operation. Period in seconds : " + (period / 1000L));
                alarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        0L,
                        period,
                        intent
                );
            } else {
                if (DEBUG)
                    Log.d(TAG, "AlarmManager already configured to trigger automatic refresh operation.");
            }
        }
    }

    private void clearAutomaticRefresh() {
        if (DEBUG)
            Log.d(TAG, "Clearing AlarmManager. Refresh operation will no more be triggered automatically");
        final PendingIntent intent = automaticRefreshPendingIntent(PendingIntent.FLAG_NO_CREATE);
        if (intent != null)
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
        if (servers.isEmpty()) {
            if (DEBUG)
                Log.d(TAG, "No observed server. Skipping");
            if (serverStatusChangedListener != null)
                serverStatusChangedListener.onAllServerStatusUpdated(servers);
            return;
        }
        //Otherwise, we use RxJava to update the status of all servers
        //First, we need to acquire a wake-lock so we are sure that the device does not goes off
        //when we are pinging all servers
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
                        server.setStatus(MinecraftServer.DetailedStatus.PINGING, true);
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
                        //If a server has gone offline
                        if (server.getDetailedStatus() == MinecraftServer.DetailedStatus.OFFLINE && server.getOfflineSince() == 0) {
                            //If the server has gone offline, we update the offline since.
                            server.setOfflineSince(server.getLastUpdateTime());
                            //We set the boolean to true if the application is running since status are update in real-time on the UI.
                            server.setHasOfflineStatusBeenSeen(bindCount > 0);
                        }
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
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * This function notify the user when a server goes offline. The notification is updated
     * as soon as another server is detected offline.
     */
    private void notifyWhenServerIsOffline() {
        //We check if the user has enabled the notification
        if (!userPreferences.notifyWhenServerGoesOffline().get()) {
            if (DEBUG)
                Log.d(TAG, "Notifications when a server goes offline are not enabled. Skipping");
            return;
        }

        //After we have updated all servers
        final Observable<MinecraftServerEntity> offlineServersObservable = Observable.from(servers)
                .filter(new Func1<MinecraftServerEntity, Boolean>() {
                    @Override
                    public Boolean call(MinecraftServerEntity server) {
                        return server.getDetailedStatus() == MinecraftServer.DetailedStatus.OFFLINE;
                    }
                });

        //We create a notification only for servers that the user does not know they are offline.
        final Observable<List<MinecraftServerEntity>> serversRequiringNotification = offlineServersObservable.asObservable()
                .filter(new Func1<MinecraftServerEntity, Boolean>() {
                    @Override
                    public Boolean call(MinecraftServerEntity minecraftServerEntity) {
                        return !minecraftServerEntity.hasOfflineStatusBeenSeen();
                    }
                })
                .toList();

        //TODO : zip the two observable to have both the list and the number of offline servers.
        Observable.zip(offlineServersObservable.count(), serversRequiringNotification, new Func2<Integer, List<MinecraftServerEntity>, Object[]>() {
                    @Override
                    public Object[] call(Integer numberOfOfflineServers, List<MinecraftServerEntity> serversRequiringNotification) {
                        return new Object[]{numberOfOfflineServers, serversRequiringNotification};
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object[]>() {
                    @Override
                    public void call(Object[] objects) {
                        final Integer numberOfOfflineServers = (Integer) objects[0];
                        final List<MinecraftServerEntity> serversRequiringNotification = (List<MinecraftServerEntity>) objects[1];

                        if (DEBUG) {
                            final StringBuilder sServersRequiringNotification = new StringBuilder();
                            for (MinecraftServerEntity e : serversRequiringNotification) {
                                if (sServersRequiringNotification.length() == 0)
                                    sServersRequiringNotification.append("'" + e.getName() + "'");
                                else
                                    sServersRequiringNotification.append(", '" + e.getName() + "'");
                            }
                            if (sServersRequiringNotification.length() == 0)
                                sServersRequiringNotification.append("(none)");
                            Log.d(TAG, "Number of offline servers : " + numberOfOfflineServers + ". Offline server(s) requiring a notification : " + sServersRequiringNotification.toString());
                        }
                        //If the service is bound to an activity, we do not notify the user
                        if (bindCount > 0) {
                            if (DEBUG)
                                Log.d(TAG, "Activity is displayed to end-user. Notification muted.");
                            notificationManager.cancel(OFFLINE_SERVER_NOTIFICATION_ID);
                        } else {
                            if (serversRequiringNotification.size() > 0) {
                                final Notification notification = buildNotificationForOfflineServers(numberOfOfflineServers, serversRequiringNotification);
                                notificationManager.notify(OFFLINE_SERVER_NOTIFICATION_ID, notification);
                            } else
                                notificationManager.cancel(OFFLINE_SERVER_NOTIFICATION_ID);
                        }
                    }
                });

    }

    private Notification buildNotificationForOfflineServers(int numberOfOfflineServers, List<MinecraftServerEntity> serversRequiringNotification) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (serversRequiringNotification.size() == 1)
            buildNotificationForOneOfflineServer(builder, numberOfOfflineServers, serversRequiringNotification.get(0));
        else
            buildNotificationForMultipleOfflineServers(builder, numberOfOfflineServers, serversRequiringNotification);

        //If notifications on wearable devices is deactivated, we had the LOCAL_ONLY flag.
        builder.setLocalOnly(!userPreferences.pushNotificationsToWearableDevices().get());

        //We want the HomeActivity to be displayed when the user click on the notification.
        final PendingIntent startHomeActivityPendingIntent = PendingIntent.getActivity(this, 0, HomeActivity_.intent(this).get(), 0);
        builder.setContentIntent(startHomeActivityPendingIntent);
        //Also the notification is canceled when the user open the application using the notification
        builder.setAutoCancel(true);

        //When the notification is canceled, we want to update the hasOfflineBeenSeen of servers in the notification
        final ArrayList<Integer> serverIds = new ArrayList<>();
        for (MinecraftServerEntity e : serversRequiringNotification)
            serverIds.add(e.getId());
        final Intent onNotificationDismissedIntent = PingService_.intent(this).onNotificationDismissed(serverIds).get();
        final PendingIntent onNotificationDismissedPendingIntent = PendingIntent.getService(this, 0, onNotificationDismissedIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setDeleteIntent(onNotificationDismissedPendingIntent);

        //Since we are refreshing the notification, we only want the sound/vibrate to be played the first time.
        builder.setOnlyAlertOnce(true);
        return builder.build();
    }

    private void buildNotificationForOneOfflineServer(NotificationCompat.Builder builder, int numberOfOfflineServers, MinecraftServerEntity offlineServer) {
        final long offlineTime = offlineServer.getLastUpdateTime() - offlineServer.getOfflineSince();
        builder
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker(getString(R.string.offlineServerNotification_oneServer_ticker, offlineServer.getName()))
                .setContentTitle(offlineServer.getName())
                .setContentText(getString(R.string.offlineServerNotification_oneServer_contentText, timeFormatter.format(offlineTime)));
        //TODO : add the favicon of the server if available. for 1.7+ servers only
    }

    private void buildNotificationForMultipleOfflineServers(NotificationCompat.Builder builder, int numberOfOfflineServers, List<MinecraftServerEntity> offlineServers) {
        final MinecraftServerEntity offlineServer = offlineServers.get(0);
        final long offlineTime = offlineServer.getLastUpdateTime() - offlineServer.getOfflineSince();
        builder
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker(getString(R.string.offlineServerNotification_multipleServers_ticker, offlineServers.size()))
                .setContentTitle(getString(R.string.offlineServerNotification_multipleServers_contentTitle, offlineServers.size()))
                .setContentText(getString(R.string.offlineServerNotification_multipleServers_contentText, offlineServer.getName(),
                        offlineServers.size() - 1, timeFormatter.format(offlineTime)));
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
        //And we notify our listener that the data set has changed
        if (serverStatusChangedListener != null)
            serverStatusChangedListener.onServerListChanged(servers);
    }

    /*package*/ void removeServer(MinecraftServerEntity server) {
        if (DEBUG)
            Log.d(TAG, String.format("Removing server '%s' from the list", server.getName()));
        //We check if the server has an ID, if not well there is nothing to remove because
        //it is not in ths list
        if (server.getId() == 0) {
            if (DEBUG)
                Log.d(TAG, String.format("Server '%s' has no id. Skipping remove from DB operation", server.getName()));
        } else {
            //We remove the server from the database if it has an id
            runtimeMinecraftServerDao.delete(server);
        }
        //Then we remove it from the list
        servers.remove(server);
        //Finally we notify our listener that the data set has changed
        if (serverStatusChangedListener != null)
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
            //We notify the user about server status
            notifyWhenServerIsOffline();
            //We release the wake-lock
            if (DEBUG)
                Log.d(TAG, "Releasing wake-lock");
            wakeLock.release();
            if (DEBUG)
                Log.d(TAG, "Refresh completed, notifying registered listener");
            if (serverStatusChangedListener != null)
                serverStatusChangedListener.onAllServerStatusUpdated(servers);
        }

        @Override
        public void onError(Throwable e) {
            //e.printStackTrace();
        }

        @Override
        public void onNext(MinecraftServerEntity server) {
            if (DEBUG)
                Log.d(TAG, String.format("Server status refreshed : %s:%d -> %s", server.getHost(), server.getPort(), server.getDetailedStatus().name()));
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
                    if (key.equals(getString(R.string.userpreferences_refreshAutomaticallyInBackground_key))) {
                        if (userPreferences.refreshAutomaticallyInBackground().get()) {
                            configureAutomaticRefresh(false);
                        } else {
                            clearAutomaticRefresh();
                        }
                    } else if (key.equals(getString(R.string.userpreferences_timeInMSBetweenChecks_key))) {
                        //We should change the timer period only if the automatic refresh is enabled by the user
                        if (userPreferences.refreshAutomaticallyInBackground().get()) {
                            clearAutomaticRefresh();
                            configureAutomaticRefresh(true);
                        }
                    }
                }

            };


}
