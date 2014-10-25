/**
 * Copyright 2014 Cyril Vlaminck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.cvlaminck.immso.minecraft.v1_7.packets.server;

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
