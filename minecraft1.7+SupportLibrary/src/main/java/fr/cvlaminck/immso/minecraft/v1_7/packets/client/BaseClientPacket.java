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
package fr.cvlaminck.immso.minecraft.v1_7.packets.client;

import fr.cvlaminck.immso.minecraft.v1_7.io.MC17DataInputStream;
import fr.cvlaminck.immso.minecraft.v1_7.packets.BasePacket;

/**
 *
 */
public abstract class BaseClientPacket
        extends BasePacket {

    protected BaseClientPacket(int packetId) {
        super(packetId);
    }

    @Override
    protected void internalReadFromInputStream(MC17DataInputStream in) {
        throw new UnsupportedOperationException("Client packet cannot be read by this library");
    }

}
