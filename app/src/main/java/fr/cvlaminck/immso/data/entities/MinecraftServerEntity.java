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
package fr.cvlaminck.immso.data.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Observable;

import fr.cvlaminck.immso.data.repositories.MinecraftServerDao;
import fr.cvlaminck.immso.minecraft.MinecraftServer;

@DatabaseTable(tableName = "servers", daoClass = MinecraftServerDao.class)
public class MinecraftServerEntity
        extends Observable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String host;

    @DatabaseField
    private int port;

    /**
     * Name of this server. This value is set by the user when
     * registering this server on its application.
     */
    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String description;

    private String version;

    @DatabaseField(canBeNull = false)
    private String toolsVersion;

    @DatabaseField
    private int protocolVersion;

    @DatabaseField
    private int numberOfPlayer = 0;

    @DatabaseField
    private int maxNumberOfPlayer = 0;

    @DatabaseField(canBeNull = false)
    private MinecraftServer.DetailedStatus detailedStatus = MinecraftServer.DetailedStatus.UNKNOWN;

    /**
     * When this server has been detected offline for the first time.
     * There is no way to detect when exactly the server has gone offline.
     * This value will be different than 0 only if the status is OFFLINE.
     */
    @DatabaseField
    private long offlineSince = 0;

    @DatabaseField
    private long lastUpdateTime = 0;

    public MinecraftServer getServer() {
        final MinecraftServer mcServer = new MinecraftServer(host, port);
        mcServer.setProtocolVersion(protocolVersion);
        return mcServer;
    }

    public void setServer(final MinecraftServer server, boolean notifyObservers) {
        host = new String(server.getHost());
        port = server.getPort();
        description = (server.getDescription() != null) ? new String(server.getDescription()) : null;
        version = server.getVersion();
        protocolVersion = server.getProtocolVersion();
        numberOfPlayer = server.getNumberOfPlayer();
        maxNumberOfPlayer = server.getMaxNumberOfPlayer();
        lastUpdateTime = server.getLastUpdateTime();
        setStatus(server.getDetailedStatus(), notifyObservers);
    }

    /**
     * Returns true if the server is actually online and responded the last ping
     * request.
     */
    public boolean isOnline() {
        return detailedStatus == MinecraftServer.DetailedStatus.ONLINE || detailedStatus == MinecraftServer.DetailedStatus.FULL;
    }

    /**
     * Return the status of the server.
     */
    public MinecraftServer.Status getStatus() {
        return detailedStatus.equivalentStatus();
    }

    public int getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getToolsVersion() {
        return toolsVersion;
    }

    public void setToolsVersion(String toolsVersion) {
        this.toolsVersion = toolsVersion;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public int getMaxNumberOfPlayer() {
        return maxNumberOfPlayer;
    }

    public void setMaxNumberOfPlayer(int maxNumberOfPlayer) {
        this.maxNumberOfPlayer = maxNumberOfPlayer;
    }

    public MinecraftServer.DetailedStatus getDetailedStatus() {
        return detailedStatus;
    }

    public void setStatus(MinecraftServer.DetailedStatus detailedStatus, boolean notifyObservers) {
        if (detailedStatus != this.detailedStatus) {
            this.detailedStatus = detailedStatus;
            setChanged();
            if (notifyObservers)
                notifyObservers();
        }
        //We reset the offlineSince only if the status changed to ONLINE
        if (detailedStatus == MinecraftServer.DetailedStatus.ONLINE) {
            offlineSince = 0;
        }
    }

    public void setDetailedStatus(MinecraftServer.DetailedStatus detailedStatus) {
        setStatus(detailedStatus, false);
    }

    public long getOfflineSince() {
        return offlineSince;
    }

    public void setOfflineSince(long offlineSince) {
        //The offlineSince value can be set only if the server is actually offline
        if (detailedStatus != MinecraftServer.DetailedStatus.OFFLINE)
            return;
        this.offlineSince = offlineSince;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
