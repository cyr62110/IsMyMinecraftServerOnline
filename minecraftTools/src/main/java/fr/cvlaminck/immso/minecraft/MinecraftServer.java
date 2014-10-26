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
package fr.cvlaminck.immso.minecraft;

/**
 *
 */
public class MinecraftServer {

    /**
     * Address of the server. May be an IP address or a domain name.
     */
    private String host;

    /**
     * Port used to connect to the server.
     */
    private int port;

    /**
     * Description of the server. This value is defined by the server admin in
     * the server.properties file.
     */
    private String description;

    /**
     * Version of Minecraft server. This value is not the exact version of the
     * server but the value used to find the PingSender that must be used to ping
     * this server and retrieve its status.
     */
    private String version;

    /**
     * Version of the Minecraft protocol used by the server to
     * communicate with clients. This number may be used internally
     * by the PingSender.
     */
    private int protocolVersion;

    /**
     * Number of player actually connected on the server.
     */
    private int numberOfPlayer;

    /**
     * Maximum number of player that can be connected on the server
     * at the same time.
     */
    private int maxNumberOfPlayer;

    /**
     * Favicon.
     * May be null
     */
    private byte[] favicon;

    /**
     * Status of the server
     */
    private DetailedStatus detailedStatus = DetailedStatus.UNKNOWN;

    /**
     * When the status has been updated
     */
    private long lastUpdateTime;

    public MinecraftServer() {
    }

    public MinecraftServer(String host, int port) {
        this.host = host;
        this.port = port;
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

    public DetailedStatus getDetailedStatus() {
        return detailedStatus;
    }

    public void setDetailedStatus(DetailedStatus detailedStatus) {
        this.detailedStatus = detailedStatus;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte[] getFavicon() {
        return favicon;
    }

    public void setFavicon(byte[] favicon) {
        this.favicon = favicon;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Status of the server
     */
    public enum Status {
        /**
         * We do not known the status of this server. For ex. if the library is actually pinging the server
         * or the server has never been pinged, etc.
         */
        UNKNOWN,
        /**
         * The server is not accessible and players cannot connect to it.
         */
        OFFLINE,
        /**
         * The server is accessible. Players may be able to connect
         * to it if not full.
         */
        ONLINE
    }

    /**
     * Enumeration of status for the server. This enumeration provide more detail on why we
     * have given the status to the server.
     */
    public enum DetailedStatus {
        /**
         * We do not know the status of the server since we have never pinged it.
         */
        UNKNOWN(Status.UNKNOWN),
        /**
         * The library is actually actualizing server status.
         */
        PINGING(Status.UNKNOWN),
        /**
         * The latest ping failed due to an internal error in the PingSender implementation.
         * This may be caused by protocol changes after an update of the server.
         */
        INTERNAL_ERROR(Status.UNKNOWN),
        /**
         * The server is not accessible or available. You cannot connect to
         * this server using your minecraft client.
         */
        OFFLINE(Status.OFFLINE),
        /**
         * The server is available.
         */
        ONLINE(Status.ONLINE),
        /**
         * The server is online but no more people can cannot connect
         * to its since it is full.
         */
        FULL(Status.ONLINE);

        private Status status;

        private DetailedStatus(Status status) {
            this.status = status;
        }

        public Status equivalentStatus() {
            return status;
        }
    }

}
