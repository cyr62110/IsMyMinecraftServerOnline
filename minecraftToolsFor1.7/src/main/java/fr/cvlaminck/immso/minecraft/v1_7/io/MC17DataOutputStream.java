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
package fr.cvlaminck.immso.minecraft.v1_7.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fr.cvlaminck.immso.minecraft.v1_7.types.String;
import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;

/**
 * Data output stream written for the Minecraft 1.7 and above
 * client-server communication. As specified in Minecraft protocol
 * documentation, DataOutputStream uses big-endian to write data on
 * the underlying stream.
 */
public class MC17DataOutputStream extends DataOutputStream {

    public MC17DataOutputStream(OutputStream out) {
        super(out);
    }

    public void writeString(String value) throws IOException {
        value.writeOnOutputStream(out);
    }

    public void writeVarInt(VarInt value) throws IOException {
        value.writeOnOutputStream(out);
    }

    public void writeVarInt(int value) throws IOException {
        this.writeVarInt(new VarInt(value));
    }

}
