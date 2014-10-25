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
