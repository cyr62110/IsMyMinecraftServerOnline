package fr.cvlaminck.immso.minecraft.v1_7.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fr.cvlaminck.immso.minecraft.v1_7.packets.server.BaseServerPacket;
import fr.cvlaminck.immso.minecraft.v1_7.packets.server.StatusResponsePacket;
import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;

/**
 * TODO
 */
public class MC17PacketInputStream {

    protected InputStream in = null;

    private static final Map<Integer, Class<? extends BaseServerPacket>> packets = new HashMap<>();

    private static final Class<?> emptyArrayOfClasses[] = new Class[0];

    static {
        packets.put(StatusResponsePacket.PACKET_ID, StatusResponsePacket.class);
    }

    public MC17PacketInputStream(InputStream in) {
        this.in = in;
    }

    //Read length + packet id, add the bytes on the top of the inputstream
    //then read the packet
    public BaseServerPacket readPacket() throws IOException {
        //First, we need to read the packet length
        final VarInt length = VarInt.readFromInputStream(in);
        //Then, the packet id
        final VarInt packetId = VarInt.readFromInputStream(in);
        //Using the packet id, we instantiate the class associated to this packet.
        final BaseServerPacket packet = instantiatePacketWithPacketId(packetId.getValue());
        //Then, we read the data of this packet
        packet.readFromInputStream(in);
        return packet;
    }

    private BaseServerPacket instantiatePacketWithPacketId(int packetId) {
        BaseServerPacket baseServerPacket = null;
        Class<? extends BaseServerPacket> packetClass = packets.get(packetId);
        if(packetClass == null)
            throw new IllegalStateException("Unsupported packet type received");
        try {
            baseServerPacket = packetClass.getConstructor(emptyArrayOfClasses).newInstance();
        } catch (Exception e) {
            e.printStackTrace(); //TODO : remove here
        }
        return baseServerPacket;
    }



}
