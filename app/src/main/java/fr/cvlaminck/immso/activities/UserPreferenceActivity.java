package fr.cvlaminck.immso.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

import fr.cvlaminck.immso.R;
import fr.cvlaminck.immso.preferences.UserPreferences_;
import fr.cvlaminck.immso.services.impl.PingService_;

@EActivity
public class UserPreferenceActivity
        extends PreferenceActivity {

    private PingServiceConnection pingServiceConnection = null;

    @Pref
    protected UserPreferences_ userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.userpreferences);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pingServiceConnection == null) {
            final Intent intent = PingService_.intent(this).get();
            pingServiceConnection = new PingServiceConnection();
            bindService(intent, pingServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @AfterViews
    protected void afterViews() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pingServiceConnection != null) {
            unbindService(pingServiceConnection);
            pingServiceConnection = null;
        }
    }

    private class PingServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
