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
        if(pingServiceConnection == null) {
            final Intent bindIntent = PingService_.intent(this).get();
            pingServiceConnection = new PingServiceConnection();
            bindService(bindIntent, pingServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pingServiceConnection != null) {
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
