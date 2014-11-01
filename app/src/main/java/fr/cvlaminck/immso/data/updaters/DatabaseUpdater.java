package fr.cvlaminck.immso.data.updaters;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfigLoader;

/**
 *
 */
public abstract class DatabaseUpdater {

    /**
     * Database version this updater will convert from.
     */
    private int fromDatabaseVersion;

    /**
     * Database version this updater will convert to.
     */
    private int toDatabaseVersion;

    public DatabaseUpdater(int from, int to) {
        this.fromDatabaseVersion = from;
        this.toDatabaseVersion = to;
    }

    public void update(final SQLiteDatabase database, final ConnectionSource connectionSource) {
        final DatabaseUpdates updates = new DatabaseUpdates(database, connectionSource);
        updatesToApply(updates);
    }

    protected abstract void updatesToApply(final DatabaseUpdates updates);

}
