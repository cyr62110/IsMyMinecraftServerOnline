package fr.cvlaminck.aboutmyapp.info.providers.app;

import fr.cvlaminck.aboutmyapp.data.entities.projects.ApplicationEntity;
import fr.cvlaminck.aboutmyapp.info.providers.InfoProvider;

/**
 * An InfoProvider for ApplicationEntity.
 *
 *
 *
 * @since 0.1
 */
public abstract interface AppInfoProvider
        extends InfoProvider<ApplicationEntity> {

    /**
     * Return application information extracted from the source used by this
     * implementation of the AppInfoProvider.
     * <p/>
     * Level allow you to choose which kind of information should be retrieved by this
     * provider, for example :
     * - -1 : All information are retrieved
     * - 0 : Only information about the app are retrieved
     * - 1 : Information about libraries used in the app are also retrieved
     * - 2 : TBD
     * You are also allowed to define custom level for your implementation of AppInfoProvider.
     * <p/>
     * Implementation can use network-based operation to retrieve information since
     * all call to this method will not be made from the UI-thread. You must ensure
     * that your implementation is thread-safe.
     *
     * @since 0.1
     */
    public ApplicationEntity getApplicationInformation(int level);

}
