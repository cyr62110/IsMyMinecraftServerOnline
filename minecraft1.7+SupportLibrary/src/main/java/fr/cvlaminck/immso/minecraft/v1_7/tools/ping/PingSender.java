package fr.cvlaminck.immso.minecraft.v1_7.tools.ping;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.net.Socket;

import fr.cvlaminck.immso.minecraft.MinecraftServer;
import fr.cvlaminck.immso.minecraft.tools.ping.exceptions.InternalErrorPingException;
import fr.cvlaminck.immso.minecraft.tools.ping.exceptions.PingException;
import fr.cvlaminck.immso.minecraft.v1_7.io.MC17PacketInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.io.MC17PacketOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.packets.client.HandshakePacket;
import fr.cvlaminck.immso.minecraft.v1_7.packets.client.StatusRequestPacket;
import fr.cvlaminck.immso.minecraft.v1_7.packets.server.StatusResponsePacket;
import fr.cvlaminck.immso.minecraft.v1_7.resources.status.StatusInformationResource;

/**
 * Implementation of a ping sender for Minecraft 1.7 and above.
 */
public class PingSender extends fr.cvlaminck.immso.minecraft.tools.ping.PingSender {

    @Override
    protected void internalPing(Socket socket, MinecraftServer mcServer) throws IOException, PingException {
        final MC17PacketInputStream in = new MC17PacketInputStream(socket.getInputStream());
        final MC17PacketOutputStream out = new MC17PacketOutputStream(socket.getOutputStream());
        //First, we need to send an handshake with the right protocol version
        final HandshakePacket handshake = new HandshakePacket();
        handshake.setServerAddress(mcServer.getHost());
        handshake.setPort(mcServer.getPort());
        handshake.setProtocolVersion(5); //TODO
        handshake.setNextState(HandshakePacket.State.STATUS);
        out.writePacket(handshake);
        //Then, we send our status request
        final StatusRequestPacket statusRequestPacket = new StatusRequestPacket();
        out.writePacket(statusRequestPacket);
        //And, we wait for our status response packet
        //TODO : Handle disconnect packet that can be sent instead of response while the server is starting
        final StatusResponsePacket statusResponsePacket;
        try {
            statusResponsePacket = (StatusResponsePacket) in.readPacket();
        } catch (JsonMappingException ex) {
            throw new InternalErrorPingException(mcServer, ex);
        }
        //Finally, we use the status that we have retrieved to fill our MinecraftServer object
        final StatusInformationResource status = statusResponsePacket.getStatus();
        mcServer.setDescription("Server pinged using " + this.getClass().getSimpleName() + " for '" + "'"); //TODO : Not supported for now
        mcServer.setVersion(status.getVersion().getName());
        mcServer.setProtocolVersion(status.getVersion().getProtocol());
        mcServer.setNumberOfPlayer(status.getPlayers().getOnline());
        mcServer.setMaxNumberOfPlayer(status.getPlayers().getMax());
        //TODO : decode and setFavicon
    }
}
