package fr.cvlaminck.immso.minecraft.v1_7.resources.status;

/**
 * Information about how many players are playing, can play, etc...
 * Serialized using JSON.
 */
public class PlayersStatusInformationResource {

    /**
     * Maximum number of player that can play at a time on
     * the server.
     */
    private int max;

    /**
     * Count of actually online players.
     */
    private int online;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
