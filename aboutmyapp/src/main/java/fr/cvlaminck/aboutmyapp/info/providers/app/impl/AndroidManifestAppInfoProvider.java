package fr.cvlaminck.aboutmyapp.info.providers.app.impl;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import fr.cvlaminck.aboutmyapp.data.entities.projects.ApplicationEntity;
import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;
import fr.cvlaminck.aboutmyapp.info.providers.Priority;
import fr.cvlaminck.aboutmyapp.info.providers.app.BaseAppInfoProvider;

/**
 * AppInfoProvider retrieving its data from the application AndroidManifest.xml.
 * This implementation does not provide any information about application dependencies nor
 * licences. It is up to the developer to set another AppInfoProvider to provide this information.
 *
 * @since 0.1
 */
public class AndroidManifestAppInfoProvider
        extends BaseAppInfoProvider
        implements AppInfoProvider {

    private boolean hasRetrievedAllInformation = false;

    /**
     * Icon associated with the application
     * TODO : Not supported for now.
     */
    private Drawable applicationIcon = null;

    /**
     * Label associated with the application
     */
    private String applicationLabel = null;

    /**
     * Name of the version currently installed on the device.
     */
    private String versionName;

    public AndroidManifestAppInfoProvider() {
    }

    private AndroidManifestAppInfoProvider(Parcel in) {
        //TODO applicationIcon
        applicationLabel = in.readString();
        versionName = in.readString();
    }

    /**
     * Retrieve all information from the application manifest using the PackageManager.
     */
    private void retrieveApplicationInformation() {
        final PackageManager packageManager = getContext().getPackageManager();
        final ApplicationInfo applicationInfo = getContext().getApplicationInfo();
        //We retrieve some information using the PackageManager directly as the icon or the label.
        applicationIcon = packageManager.getApplicationIcon(applicationInfo);
        applicationLabel = packageManager.getApplicationLabel(applicationInfo).toString();
        //Finally we retrieve the PackageInfo to get the current version of our app, etc.
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);

            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    @Override
    public int getPriority() {
        return Priority.MEDIUM.value();
    }

    @Override
    public boolean usesNetwork() {
        return false;
    }

    @Override
    public ApplicationEntity getApplicationInformation(int level) {
        if (!hasRetrievedAllInformation)
            retrieveApplicationInformation();
        final ApplicationEntity application = new ApplicationEntity();
        //TODO application icon
        application.setName(applicationLabel);
        application.setVersion(versionName);
        return application;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {

    }

    private static final Parcelable.Creator<AndroidManifestAppInfoProvider> CREATOR = new Parcelable.Creator<AndroidManifestAppInfoProvider>() {
        @Override
        public AndroidManifestAppInfoProvider createFromParcel(Parcel in) {
            return new AndroidManifestAppInfoProvider(in);
        }

        @Override
        public AndroidManifestAppInfoProvider[] newArray(int size) {
            return new AndroidManifestAppInfoProvider[size];
        }
    };

}
