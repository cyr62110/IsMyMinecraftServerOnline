package fr.cvlaminck.aboutmyapp.info.providers;

import android.content.Context;
import android.os.Parcelable;

/**
 * An InfoProvider read all information about something from a source and provide them to
 * the library so they can be displayed in library dialogs and activities. Some provider
 * may requires you to add some permission to your Manifest.
 *
 * All AppInfoProviders are not designed to provides all information that may be contained
 * in the resulting entity, so information will be merged. In case of conflict, the merge
 * operation will always choose to keep information coming from the provider with the highest priority.
 * (The one with the lowest priority value)
 *
 * All provider must be fully compliant with the Parcelable interface. See http://developer.android.com/reference/android/os/Parcelable.html
 *
 * @since 0.1
 */
public interface InfoProvider<T>
    extends Parcelable {

    /**
     * InfoProvider may require a Context to retrieve information. This one will
     * always be provided through this method by the library before calling any other
     * method of the interface.
     *
     * @since 0.1
     */
    public void setContext(Context context);

    /**
     * Priority of this InfoProvider. The lowest value is the value returned by this method, the
     * higher is the priority of this provider.
     *
     * @since 0.1
     */
    public int getPriority();

    /**
     * Indicate whether or not this implementation uses the network to retrieve information
     * or not.
     *
     * @since 0.1
     */
    public boolean usesNetwork();

    /**
     *
     *
     * @since 0.1
     */
    public T get(int level);

}

