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

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataOutputStream;
import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 *
 */
public abstract class BaseServerPacket
        extends BasePacket {

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
