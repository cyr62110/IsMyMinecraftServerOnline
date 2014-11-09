package fr.cvlaminck.aboutmyapp.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A wrapper that allow developer to transform Drawable into Parcelable.
 *
 * @since 0.1
 */
public class ParcelableDrawableWrapper
        extends Drawable
        implements Parcelable{

    /**
     * Drawable wrapped into this instance of the class.
     */
    private Drawable wrappedDrawable = null;

    @Override
    public void draw(Canvas canvas) {
        if(wrappedDrawable == null) {

        } else
            wrappedDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        if(wrappedDrawable == null) {

        } else
            wrappedDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if(wrappedDrawable == null) {

        } else
            wrappedDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        if(wrappedDrawable == null) {

            return 0;
        } else
            return wrappedDrawable.getOpacity();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //Depending of the type of drawable used, we need to serialize
    }
}
