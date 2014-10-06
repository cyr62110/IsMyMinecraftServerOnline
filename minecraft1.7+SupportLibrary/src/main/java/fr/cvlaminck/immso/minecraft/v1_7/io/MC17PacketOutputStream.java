package fr.cvlaminck.immso.minecraft.v1_7.io;

import java.io.IOException;
import java.io.OutputStream;

import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 * OutputStream specialized in sending packet to a Minecraft 1.7 server.
 */
public class MC17PacketOutputStream {

    protected OutputStream out;

    public MC17PacketOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writePacket(BasePacket packet) throws IOException {
        packet.writeOnOutputStream(out);
        //We flush the stream to be sure that the packet has been sent
        out.flush();
    }

}
