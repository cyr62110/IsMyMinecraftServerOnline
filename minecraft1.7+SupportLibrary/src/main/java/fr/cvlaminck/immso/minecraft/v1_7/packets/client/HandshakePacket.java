package fr.cvlaminck.immso.minecraft.v1_7.packets.client;

import java.io.IOException;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.types.String;
import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;

/**
 * This packet is the one send by the client to establish the communication with
 * the server.
 */
public class HandshakePacket
        extends BaseClientPacket {
    public final static int PACKET_ID = 0x00;

    private VarInt protocolVersion = null;

    private String serverAddress = null;

    private int port = 0; //unsigned short

    private State nextState = null;

    public HandshakePacket() {
        super(PACKET_ID);
    }

    @Override
    protected int internalGetLength() {
        return protocolVersion.getLength() + serverAddress.getLength() +
                2 + nextState.value.getLength();
    }

    @Override
    protected void internalWriteOnOutputStream(MC17DataOutputStream out) throws IOException {
        out.writeVarInt(protocolVersion);
        out.writeString(serverAddress);
        out.writeShort(port);
        out.writeVarInt(nextState.value());
    }

    public int getProtocolVersion() {
        return protocolVersion.getValue();
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = new VarInt(protocolVersion);
    }

    public java.lang.String getServerAddress() {
        return serverAddress.getValue();
    }

    public void setServerAddress(java.lang.String serverAddress) {
        this.serverAddress = new String(serverAddress);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Port value must be [0; 65535]");
        this.port = port;
    }

    public int getNextState() {
        return nextState.value().getValue();
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    public static enum State {
        STATUS(1),
        LOGIN(2);

        private VarInt value;

        private State(int stateValue) {
            this.value = new VarInt(stateValue);
        }

        public VarInt value() {
            return value;
        }

        public static State from(VarInt nextState) {
            for (State state : values()) {
                if (state.value.equals(nextState))
                    return state;
            }
            throw new IllegalArgumentException("Provided value does not correspond to a valid state.");
        }
    }
}
