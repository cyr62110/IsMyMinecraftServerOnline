package fr.cvlaminck.immso.data.updaters.versions;

import fr.cvlaminck.immso.data.updaters.DatabaseUpdater;
import fr.cvlaminck.immso.data.updaters.DatabaseUpdates;

/**
 * DatabaseUpdater that allow to upgrade the database to the second version.
 */
public class V1ToV2DatabaseUpdater extends DatabaseUpdater {

    public V1ToV2DatabaseUpdater(int from, int to) {
        super(from, to);
    }

    @Override
    protected void updatesToApply(DatabaseUpdates updates) {

    }

}
