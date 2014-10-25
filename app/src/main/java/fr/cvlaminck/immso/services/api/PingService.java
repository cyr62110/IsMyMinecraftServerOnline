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
package fr.cvlaminck.immso.services.api;

import java.util.List;

import fr.cvlaminck.immso.data.entities.MinecraftServerEntity;

public interface PingService {

    /**
     * Get the list of observed servers.
     */
    List<MinecraftServerEntity> getServers();

    /**
     * Add a new server in the list of observed servers.
     * This does NOT trigger an automatic refresh operation.
     *
     * @param minecraftServer Server to observe
     */
    void addServer(MinecraftServerEntity minecraftServer);

    /**
     * Remove a server from the list of observed servers.
     *
     * @param minecraftServer Server the user do not want to observe anymore
     */
    void removeServer(MinecraftServerEntity minecraftServer);

    /**
     * Refresh the status of all observed servers.
     */
    void refreshServerStatus();

    void registerServerStatusChangedListener(ServerStatusChangedListener listener);

    void unregisterServerStatusChangedListener(ServerStatusChangedListener listener);

    interface ServerStatusChangedListener {

        /**
         * Callback function called when a new server is added to the list of observed
         * server or one is removed. This callback is no called when the list is loaded
         * from the database.
         */
        public void onServerListChanged(List<MinecraftServerEntity> servers);

        /**
         * Called when the refresh operation is finished and the background task has finished
         * to actualize all observed server status.
         */
        public void onAllServerStatusUpdated(List<MinecraftServerEntity> servers);

    }
}
