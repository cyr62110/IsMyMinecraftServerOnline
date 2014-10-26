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
package fr.cvlaminck.immso.minecraft.v1_7.packets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;

/**
 *
 */
public abstract class BasePacket {

    /**
     * This field contains the length of the packet only if this packet
     * have been received through the network.
     */
    private VarInt length;

    /**
     * Id of the packet
     */
    private VarInt packetId;

    protected BasePacket(int packetId) {
        this.packetId = new VarInt(packetId);
    }

    /**
     * This method compute the length in bytes of all fields included in
     * the packet by subclasses.
     */
    protected abstract int internalGetLength();

    /**
     * Length of the packet in bytes that will be sent or that have been
     * received. Also the length field is not included in the length of
     * the packet. In case of the packet will be sent, the value is computed.
     * Otherwise, this method returned the value received.
     */
    public int getLength() {
        if (length != null) {
            return length.getValue();
        } else {
            return internalGetLength() + packetId.getLength();
        }
    }

    /**
     * This method allows subclasses to write their fields on the output stream.
     */
    protected abstract void internalWriteOnOutputStream(final MC17DataOutputStream out) throws IOException;

    /**
     * Write the packet on the OutputStream.
     */
    public void writeOnOutputStream(final OutputStream out) throws IOException {
        final MC17DataOutputStream dout = new MC17DataOutputStream(out);
        //Write the length of the packet
        dout.writeVarInt(getLength());
        //Write the packet id
        dout.writeVarInt(packetId);
        //Then write the data.
        internalWriteOnOutputStream(dout);
    }

    protected abstract void internalReadFromInputStream(final MC17DataInputStream in) throws IOException;

    /**
     * Read packet fields from the InputStream. Both length and packetId are
     * ignored since they already have been used to instantiate this packet.
     */
    public void readFromInputStream(final InputStream in) throws IOException {
        final MC17DataInputStream din = new MC17DataInputStream(in);
        internalReadFromInputStream(din);
    }

}
