package fr.cvlaminck.immso.minecraft.v1_7.resources.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Information about the Minecraft Server that are returned in the
 * status response. Those information are serialized in JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusInformationResource {

    private VersionStatusInformationResource version = null;

    private PlayersStatusInformationResource players = null;

    private String favicon = null;

    public VersionStatusInformationResource getVersion() {
        return version;
    }

    public void setVersion(VersionStatusInformationResource version) {
        this.version = version;
    }

    public PlayersStatusInformationResource getPlayers() {
        return players;
    }

    public void setPlayers(PlayersStatusInformationResource players) {
        this.players = players;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
}
