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
package fr.cvlaminck.immso.preferences;

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

    @DefaultBoolean(value = true, keyRes = R.string.userpreferences_pushNotificationsToWearableDevices_key)
    boolean pushNotificationsToWearableDevices();

}
