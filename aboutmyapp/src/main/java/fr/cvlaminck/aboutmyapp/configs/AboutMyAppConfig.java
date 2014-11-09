package fr.cvlaminck.aboutmyapp.configs;

import android.os.Parcel;
import android.os.Parcelable;

import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;

/**
 * Configuration of the library.
 */
public class AboutMyAppConfig
        implements Parcelable {

    /**
     * Configuration of the dialog displayed by the library.
     */
    private AboutMyAppDialogConfig dialogConfig = null;

    private AppInfoProvider[] appInfoProviders = null;

    public AboutMyAppConfig() {
    }

    private AboutMyAppConfig(Parcel in) {
        dialogConfig = in.readParcelable(this.getClass().getClassLoader());
        appInfoProviders = (AppInfoProvider[]) in.readParcelableArray(this.getClass().getClassLoader());
    }

    public AboutMyAppDialogConfig getDialogConfig() {
        return dialogConfig;
    }

    public void setDialogConfig(AboutMyAppDialogConfig dialogConfig) {
        this.dialogConfig = dialogConfig;
    }

    public AppInfoProvider[] getAppInfoProviders() {
        return appInfoProviders;
    }

    public void setAppInfoProviders(AppInfoProvider[] providers) {
        this.appInfoProviders = providers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeParcelable(dialogConfig, flag);
        out.writeParcelableArray(appInfoProviders, flag);
    }

    public static Parcelable.Creator<AboutMyAppConfig> CREATOR = new Parcelable.Creator<AboutMyAppConfig>()
    {
        @Override
        public AboutMyAppConfig createFromParcel(Parcel parcel) {
            return new AboutMyAppConfig(parcel);
        }

        @Override
        public AboutMyAppConfig[] newArray(int size) {
            return new AboutMyAppConfig[size];
        }
    };

}
