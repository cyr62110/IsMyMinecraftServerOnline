package fr.cvlaminck.immso.minecraft.v1_7.resources.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Version information returned in the status information.
 * Serialized using JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionStatusInformationResource {

    /**
     * Name of the Minecraft version running on the server.
     * ex. "1.7.10"
     */
    public String name;

    /**
     * Version of the protocol used to communicate with this server.
     * ex. 5
     */
    public int protocol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
}
