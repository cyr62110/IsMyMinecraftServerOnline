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
package fr.cvlaminck.immso.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Date;

import fr.cvlaminck.immso.preferences.InternalPreferences_;
import fr.cvlaminck.immso.preferences.UserPreferences_;
import fr.cvlaminck.immso.services.api.PingService;
import fr.cvlaminck.immso.services.impl.PingService_;

@EReceiver
public class RefreshReceiver
        extends BroadcastReceiver {
    private final static String TAG = RefreshReceiver.class.getSimpleName();
    private final static boolean DEBUG = false;

    @Pref
    protected UserPreferences_ userPreferences;

    @Pref
    protected InternalPreferences_ internalPreferences;

    private PingServiceConnection pingServiceConnection = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) {
            final long time = new Date().getTime();
            final long userDefinedPeriod = userPreferences.timeInMSBetweenChecks().get(); //Period defined by the user
            final long period = time - internalPreferences.lastAutomaticRefresh().getOr(time); //Period measured
            final long jitter = period - userDefinedPeriod;
            if (period != 0)
                Log.d(TAG, String.format("Automatic refresh. Time in seconds elapsed since last automatic refresh : %d (jitter : %d)",
                        period / 1000L, jitter / 1000L));
            else
                Log.d(TAG, String.format("First automatic refresh. Next one should occur in %d seconds", userDefinedPeriod / 1000L));
        }
        //We refresh server status
        PingService_.intent(context)
                .refreshAction()
                .start();
        //Finally, we update the time in the preference
        internalPreferences.lastAutomaticRefresh().put(new Date().getTime());
    }

    private class PingServiceConnection
            implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //When the service is connected, we launch the refresh operation
            final PingService pingService = (PingService) iBinder;
            pingService.refreshServerStatus();
            //And we unbind our service

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}
