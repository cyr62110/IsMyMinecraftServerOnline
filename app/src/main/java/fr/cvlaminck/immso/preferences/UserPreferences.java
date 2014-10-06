package fr.cvlaminck.immso.preferences;

import android.app.ActivityManager;
import android.app.AlarmManager;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import fr.cvlaminck.immso.R;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface UserPreferences {

    /**
     * Should the application refresh server status in background even if the
     * application is not started.
     */
    @DefaultBoolean(value = true, keyRes = R.string.userpreferences_refreshAutomaticallyInBackground_key)
    boolean refreshAutomaticallyInBackground();

    /**
     * Time in millisecond between two checks of server status.
     * This time is one of AlarmManager.INTERVAL_* constant used with
     * setInexactRepeating
     */
    @DefaultLong(value = AlarmManager.INTERVAL_FIFTEEN_MINUTES, keyRes = R.string.userpreferences_timeInMSBetweenChecks_key)
    long timeInMSBetweenChecks();

    /**
     * Notify the user using system notification when a observed server went down.
     */
    @DefaultBoolean(value = true, keyRes = R.string.userpreferences_notifyWhenServerGoesOffline_key)
    boolean notifyWhenServerGoesOffline();

}
