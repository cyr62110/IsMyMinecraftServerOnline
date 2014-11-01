package fr.cvlaminck.immso.data.updaters;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;

/**
 * DatabaseUpdater that allow to upgrade the database to the second version.
 */
public class V1ToV2DatabaseUpdater {

    public V1ToV2DatabaseUpdater() {
    }

    public void update(SQLiteDatabase database, ConnectionSource connectionSource) {
        database.execSQL("ALTER TABLE minecraftserverentity ADD hasOfflineStatusBeenSeen BOOLEAN");
    }

}
