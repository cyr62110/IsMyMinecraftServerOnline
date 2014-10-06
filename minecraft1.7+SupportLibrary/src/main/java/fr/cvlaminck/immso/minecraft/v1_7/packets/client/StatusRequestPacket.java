package fr.cvlaminck.immso.minecraft.v1_7.packets.client;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;

/**
 *
 */
public class StatusRequestPacket
        extends BaseClientPacket {
    public final static int PACKET_ID = 0x00;

    public StatusRequestPacket() {
        super(PACKET_ID);
    }

    @Override
    protected int internalGetLength() {
        return 0;
    }

    @Override
    protected void internalWriteOnOutputStream(MC17DataOutputStream out) {
        //Nothing to see here, this packet is totally empty ;)
    }
}
