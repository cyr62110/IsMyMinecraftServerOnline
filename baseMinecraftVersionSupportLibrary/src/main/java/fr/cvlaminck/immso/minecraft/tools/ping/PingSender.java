package fr.cvlaminck.immso.minecraft.tools.ping;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import fr.cvlaminck.immso.minecraft.MinecraftServer;
import fr.cvlaminck.immso.minecraft.tools.ping.exceptions.InternalErrorPingException;
import fr.cvlaminck.immso.minecraft.tools.ping.exceptions.PingException;

/**
 * A PingSender check the status of a server and returns its information.
 * PingSender uses blocking network operation, you need to run them into
 * a thread or an AsyncTask in order to not block the UI.
 */
public abstract class PingSender {
    private final static boolean DEBUG = true;

    /**
     * Ping the server
     */
    public MinecraftServer ping(String host, int port) {
        final MinecraftServer mcServer = new MinecraftServer(host, port);
        return ping(mcServer);
    }

    public MinecraftServer ping(MinecraftServer mcServer) {
        Socket socket = null;
        mcServer.setLastUpdateTime(new Date().getTime());
        //We change the status to PINGING
        mcServer.setDetailedStatus(MinecraftServer.DetailedStatus.PINGING);
        try {
            //First, we create the socket and connect to the server
            socket = new Socket(mcServer.getHost(), mcServer.getPort());
            //We retrieve all the information about the server using the internalPing method
            internalPing(socket, mcServer);
            //Finally we update the status
            if(mcServer.getNumberOfPlayer() >= mcServer.getMaxNumberOfPlayer())
                mcServer.setDetailedStatus(MinecraftServer.DetailedStatus.FULL);
            else
                mcServer.setDetailedStatus(MinecraftServer.DetailedStatus.ONLINE);
        } catch (InternalErrorPingException ex) {
            mcServer.setDetailedStatus(MinecraftServer.DetailedStatus.INTERNAL_ERROR);
            if(DEBUG)
                ex.printStackTrace();
        } catch (Exception ex) {
            mcServer.setDetailedStatus(MinecraftServer.DetailedStatus.OFFLINE);
            //TODO : handle errors like DNS error
            //TODO : handle server starting issue using the disconnection packet
            if(DEBUG)
                ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(socket);
        }
        return mcServer;
    }

    protected abstract void internalPing(Socket socket, MinecraftServer mcServer) throws IOException, PingException;

}
