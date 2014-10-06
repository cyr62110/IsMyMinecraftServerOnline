package fr.cvlaminck.immso.minecraft.v1_7.packets.client;

import java.io.IOException;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 *
 */
public abstract class BaseClientPacket
    extends BasePacket{

    protected BaseClientPacket(int packetId) {
        super(packetId);
    }

    @Override
    protected void internalReadFromInputStream(MC17DataInputStream in) {
        throw new UnsupportedOperationException("Client packet cannot be read by this library");
    }

}
