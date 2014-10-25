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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import fr.cvlaminck.immso.services.api.PingService;
import fr.cvlaminck.immso.services.impl.PingService_;

public abstract class BaseActivity
        extends Activity {

    private PingServiceConnection pingServiceConnection = null;
    protected PingService pingService = null;

    @Override
    protected void onStart() {
        super.onStart();
        if (pingServiceConnection == null) {
            final Intent bindIntent = PingService_.intent(this).get();
            pingServiceConnection = new PingServiceConnection();
            bindService(bindIntent, pingServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pingServiceConnection != null) {
            unbindService(pingServiceConnection);
            pingServiceConnection = null;
        }
    }

    protected abstract void onPingServiceConnected(final PingService pingService);

    protected void onPingServerDisconnected(final PingService pingService) {

    }

    private class PingServiceConnection
            implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            pingService = (PingService) iBinder;
            onPingServiceConnected(pingService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            onPingServerDisconnected(pingService);
            pingService = null;
        }
    }

}
