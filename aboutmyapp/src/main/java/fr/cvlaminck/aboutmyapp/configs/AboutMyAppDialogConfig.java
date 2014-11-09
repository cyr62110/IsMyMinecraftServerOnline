package fr.cvlaminck.aboutmyapp.configs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class AboutMyAppDialogConfig
    implements Parcelable {

    /**
     * Id of the layout to use in the dialog.
     */
    private int layout;

    /**
     * Title of the dialog
     */
    private String title;

    public AboutMyAppDialogConfig() {
    }

    private AboutMyAppDialogConfig(Parcel in) {
        layout = in.readInt();
        title = in.readString();
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(layout);
        out.writeString(title);
    }

    public static final Parcelable.Creator<AboutMyAppDialogConfig> CREATOR = new Creator<AboutMyAppDialogConfig>() {
        @Override
        public AboutMyAppDialogConfig createFromParcel(Parcel parcel) {
            return new AboutMyAppDialogConfig(parcel);
        }

        @Override
        public AboutMyAppDialogConfig[] newArray(int size) {
            return new AboutMyAppDialogConfig[size];
        }
    };

}
