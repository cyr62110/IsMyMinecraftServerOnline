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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.cvlaminck.immso.minecraft.v1_7.types.String;
import fr.cvlaminck.immso.minecraft.v1_7.types.VarInt;

/**
 * Data input stream written to support Minecraft 1.7+ server protocols.
 */
public class MC17DataInputStream extends DataInputStream {

    public MC17DataInputStream(InputStream in) {
        super(in);
    }

    public VarInt readVarInt() throws IOException {
        return VarInt.readFromInputStream(in);
    }

    public String readString() throws IOException {
        return String.readFromInputStream(in);
    }

}
