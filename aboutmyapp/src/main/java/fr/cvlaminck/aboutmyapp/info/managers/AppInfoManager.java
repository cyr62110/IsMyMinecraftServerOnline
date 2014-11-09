package fr.cvlaminck.aboutmyapp.info.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.cvlaminck.aboutmyapp.configs.AboutMyAppConfig;
import fr.cvlaminck.aboutmyapp.data.entities.projects.ApplicationEntity;
import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;

/**
 *
 */
public class AppInfoManager {

    private Context context = null;

    private AboutMyAppConfig config = null;

    private AppInfoManager(Context context, AboutMyAppConfig config) {
        this.context = context;
        this.config = config;
    }

    /**
     * Retrieve information about your application using the providers configured in the library
     * config.
     * <p/>
     * After each provider configured has returned its information, information about the component
     * are merged then emitted to the listener.
     *
     * @param onInformationUpdatedListener Listener that will be called when information are updated.
     * @param doNotUseNetwork              Provider that have declared using network will not be used.
     */
    public void getInformation(OnInformationUpdatedListener onInformationUpdatedListener, boolean doNotUseNetwork) {
        //First, we retrieve all the provider configured in the library configuration
        List<AppInfoProvider> providers = new ArrayList<AppInfoProvider>();
        providers.addAll(providers);
        //Then we sort them, so providers not using network comes first ordered by priority then providers using network
        Collections.sort(providers, new Comparator<AppInfoProvider>() {
            @Override
            public int compare(AppInfoProvider appInfoProvider, AppInfoProvider appInfoProvider2) {
                if (appInfoProvider.usesNetwork() == appInfoProvider2.usesNetwork()) {
                    return appInfoProvider.getPriority() - appInfoProvider2.getPriority();
                } else {
                    return (appInfoProvider.usesNetwork()) ? 1 : -1;
                }
            }
        });
        //Finally, we run the providers
        //TODO
        return;
    }

    public interface OnInformationUpdatedListener {

        public void onInformationUpdated(ApplicationEntity application);

    }

    ;

}
