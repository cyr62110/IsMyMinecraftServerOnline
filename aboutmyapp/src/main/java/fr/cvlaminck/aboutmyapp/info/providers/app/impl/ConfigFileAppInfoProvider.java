package fr.cvlaminck.aboutmyapp.info.providers.app.impl;

import android.os.Parcel;

import fr.cvlaminck.aboutmyapp.data.entities.projects.ApplicationEntity;
import fr.cvlaminck.aboutmyapp.info.providers.Priority;
import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;
import fr.cvlaminck.aboutmyapp.info.providers.app.BaseAppInfoProvider;

/**
 * AppInfoProvider retrieving its information in a configuration file in the assets : "aboutmyapp.json".
 * This implementation does not uses network and have the highest priority.
 *
 * @since 0.1
 */
public class ConfigFileAppInfoProvider
    extends BaseAppInfoProvider
    implements AppInfoProvider {

    public ConfigFileAppInfoProvider() {
    }

    private ConfigFileAppInfoProvider(Parcel in) {
        //TODO read the name of the file in the assets
        //TODO maybe also allow the file to be in raw resources
    }

    @Override
    public ApplicationEntity getApplicationInformation(int level) {
        return null;
    }

    @Override
    public int getPriority() {
        return Priority.HIGHEST.value();
    }

    @Override
    public boolean usesNetwork() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {

    }

    public final static Creator<ConfigFileAppInfoProvider> CREATOR = new Creator<ConfigFileAppInfoProvider>() {
        @Override
        public ConfigFileAppInfoProvider createFromParcel(Parcel in) {
            return null;
        }

        @Override
        public ConfigFileAppInfoProvider[] newArray(int size) {
            return new ConfigFileAppInfoProvider[size];
        }
    };

}
