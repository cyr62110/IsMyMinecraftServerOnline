package fr.cvlaminck.immso.minecraft.v1_7.packets.server;

import java.io.IOException;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 *
 */
public abstract class BaseServerPacket
    extends BasePacket{

    protected BaseServerPacket(int packetId) {
        super(packetId);
    }

    @Override
    protected int internalGetLength() {
        throw new UnsupportedOperationException("Server packet length is transmitted and must not be computed");
    }

    @Override
    protected void internalWriteOnOutputStream(MC17DataOutputStream out) {
        throw new UnsupportedOperationException("Server packet cannot be written by this library");
    }

}
