package fr.cvlaminck.immso.data.repositories;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;

public class MinecraftServerDao
        extends BaseDaoImpl<MinecraftServerEntity, Integer> {

    public MinecraftServerDao(Class<MinecraftServerEntity> dataClass) throws SQLException {
        super(dataClass);
    }

    public MinecraftServerDao(ConnectionSource connectionSource, Class<MinecraftServerEntity> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public MinecraftServerDao(ConnectionSource connectionSource, DatabaseTableConfig<MinecraftServerEntity> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

}
