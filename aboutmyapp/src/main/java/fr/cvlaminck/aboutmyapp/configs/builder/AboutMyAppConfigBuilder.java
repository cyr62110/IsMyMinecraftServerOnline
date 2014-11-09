package fr.cvlaminck.aboutmyapp.configs.builder;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.cvlaminck.aboutmyapp.configs.AboutMyAppConfig;
import fr.cvlaminck.aboutmyapp.configs.AboutMyAppDialogConfig;
import fr.cvlaminck.aboutmyapp.info.providers.app.AppInfoProvider;

public class AboutMyAppConfigBuilder {

    private Context context = null;

    private AboutMyAppDialogConfigBuilder dialogConfigBuilder = null;

    private List<AppInfoProvider> providers = null;

    public AboutMyAppConfigBuilder(Context context) {
        this.context = context;
        this.dialogConfigBuilder = new AboutMyAppDialogConfigBuilder(context);
        this.providers = new ArrayList<>();
    }

    public AboutMyAppDialogConfigBuilder dialog() {
        return dialogConfigBuilder;
    }

    /**
     * Append a new AppInfoProvider to the list of configured providers for application.
     * By default, the list is empty. If the list is still empty when the configuration is built
     * then the Builder will automatically add some pre-configured providers.
     *
     * @since 0.1
     */
    public AboutMyAppConfigBuilder appInfoProvider(AppInfoProvider appInfoProvider) {
        if(appInfoProvider != null)
            providers.add(appInfoProvider);
        return this;
    }

    /**
     * Replace the list of configured providers by the list passed as parameters.
     * It the list is null, then the configured list will be emptied.
     *
     * @since 0.1
     */
    public AboutMyAppConfigBuilder appInfoProviders(Collection<AppInfoProvider> appInfoProviders) {
        appInfoProviders.clear();
        if(appInfoProviders != null && !appInfoProviders.isEmpty())
            appInfoProviders.addAll(appInfoProviders);
        return this;
    }

    public AboutMyAppConfig build() {
        final AboutMyAppConfig config = new AboutMyAppConfig();

        //Providers that will retrieve information about the application
        //AppInfoProviders


        //We
        config.setDialogConfig(dialogConfigBuilder.build());

        return config;
    }

}
