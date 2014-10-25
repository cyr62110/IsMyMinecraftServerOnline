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

import java.io.IOException;
import java.io.OutputStream;

import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 * OutputStream specialized in sending packet to a Minecraft 1.7 server.
 */
public class MC17PacketOutputStream {

    protected OutputStream out;

    public MC17PacketOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writePacket(BasePacket packet) throws IOException {
        packet.writeOnOutputStream(out);
        //We flush the stream to be sure that the packet has been sent
        out.flush();
    }

}
