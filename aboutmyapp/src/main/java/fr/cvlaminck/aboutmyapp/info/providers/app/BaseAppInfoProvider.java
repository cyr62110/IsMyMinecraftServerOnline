package fr.cvlaminck.aboutmyapp.info.providers.app;

import android.content.Context;

import fr.cvlaminck.aboutmyapp.data.entities.projects.ApplicationEntity;
import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;

/**
 * Base class used for all implementation or AppInfoProvider.
 *
 * @since 0.1
 */
public abstract class BaseAppInfoProvider
    implements AppInfoProvider {

    private Context context = null;

    protected BaseAppInfoProvider() {
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public ApplicationEntity get(int level) {
        //TODO : Add some caching here ?
        return getApplicationInformation(level);
    }
}
