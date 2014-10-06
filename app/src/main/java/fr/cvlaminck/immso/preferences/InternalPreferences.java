package fr.cvlaminck.immso.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface InternalPreferences {

    /**
     * When server status has last been refreshed using the RefreshReceiver.
     * Internal statistics used for debug.
     */
    long lastAutomaticRefresh();

}
