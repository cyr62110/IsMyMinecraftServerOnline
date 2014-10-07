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
