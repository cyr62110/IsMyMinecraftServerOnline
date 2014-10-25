/**
 * Copyright 2014 Cyril Vlaminck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.cvlaminck.immso.data.repositories;

import com.j256.ormlite.dao.BaseDaoImpl;
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
