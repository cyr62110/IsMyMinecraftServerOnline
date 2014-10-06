package fr.cvlaminck.immso.minecraft.v1_7.packets.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.resources.status.StatusInformationResource;
import fr.cvlaminck.immso.minecraft.v1_7.types.String;
import fr.cvlaminck.immso.minecraft.v1_7.utils.SerializationUtils;

/**
 *
 */
public class StatusResponsePacket
        extends BaseServerPacket {
    public static final int PACKET_ID = 0;

    private String jsonResponse = null;

    private StatusInformationResource status;

    public StatusResponsePacket() {
        super(PACKET_ID);
    }

    @Override
    protected void internalWriteOnOutputStream(MC17DataOutputStream out) {
        //Server packet are not written
        throw new UnsupportedOperationException("Server packet cannot be written by this library");
    }

    @Override
    protected void internalReadFromInputStream(MC17DataInputStream in) throws IOException {
        jsonResponse = in.readString();
        parseInformation();
    }

    private void parseInformation() throws IOException {
        final ObjectMapper objectMapper = SerializationUtils.objectMapper();
        status = objectMapper.readValue(jsonResponse.getValue(), StatusInformationResource.class);
    }

    public StatusInformationResource getStatus() {
        return status;
    }
}
