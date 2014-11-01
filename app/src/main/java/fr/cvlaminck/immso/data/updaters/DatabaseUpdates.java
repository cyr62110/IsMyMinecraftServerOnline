package fr.cvlaminck.immso.data.updaters;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * This class allow you to update its database by listing
 * all the updates he has made on its model.
 */
public class DatabaseUpdates {
    private final static String TAG = DatabaseUpdates.class.getSimpleName();
    private final static boolean DEBUG = true;

    /**
     * Database to update
     */
    private SQLiteDatabase database;

    /**
     * Connection source to use to communicate with the database
     * through ORMLite.
     */
    private ConnectionSource connectionSource;

    public DatabaseUpdates(SQLiteDatabase database, ConnectionSource connectionSource) {
        this.database = database;
        this.connectionSource = connectionSource;
    }

    /**
     * Return the table config for the provided class.
     * The config is read from annotations or retrieved from a local cache.
     * TODO : Cache the config to be reusable
     */
    private DatabaseTableConfig getConfigForClass(Class<?> modelClass) throws SQLException {
        return DatabaseTableConfigUtil.fromClass(connectionSource, modelClass);
    }

    private DatabaseFieldConfig getConfigForField(Class<?> table, String fieldName) throws SQLException {
        final DatabaseTableConfig tableConfig = getConfigForClass(table);
        final List<DatabaseFieldConfig> fieldConfigs = tableConfig.getFieldConfigs();
        for(DatabaseFieldConfig fieldConfig : fieldConfigs) {
            if(fieldConfig.getColumnName().equals(fieldName))
                return fieldConfig;
        }
        return null;
    }

    /**
     * A field has been added in one of the existing table of the database.
     * /!\ This field must not exists in the previous version of the database.
     */
    public DatabaseUpdates addedField(Class<?> table, String fieldName, Object defaultValue) throws SQLException {
        final DatabaseFieldConfig fieldConfig = getConfigForField(table, fieldName);
        if(fieldConfig == null)
            throw new IllegalArgumentException("Field " + fieldName + " does not exists in the current version of table " + table.getSimpleName()); //TODO do a proper exception
        //We only apply the modification to the database if the field is persisted
        if(fieldConfig.isPersisted()) {
            Log.d(TAG, "test");
        }
        return this;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
