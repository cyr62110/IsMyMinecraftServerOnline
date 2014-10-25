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
package fr.cvlaminck.immso.minecraft.v1_7.tools;

import fr.cvlaminck.immso.minecraft.MinecraftToolsVersion;
import fr.cvlaminck.immso.minecraft.SupportedToolsVersions;
import fr.cvlaminck.immso.minecraft.tools.MinecraftTools;
import fr.cvlaminck.immso.minecraft.tools.ping.PingSender;

/**
 *
 */
public class Minecraft1_7Tools
        extends MinecraftTools {
    private static final MinecraftToolsVersion VERSION = new MinecraftToolsVersion("1.7.x - 1.8.x", "1.7+");

    private fr.cvlaminck.immso.minecraft.v1_7.tools.ping.PingSender pingSender = null;

    static {
        SupportedToolsVersions.getInstance().registerTools(new Minecraft1_7Tools());
    }

    protected Minecraft1_7Tools() {
        super(VERSION);
    }

    @Override
    public PingSender pingSender() {
        if (pingSender == null) {
            synchronized (this) {
                if (pingSender == null)
                    pingSender = new fr.cvlaminck.immso.minecraft.v1_7.tools.ping.PingSender();
            }
        }
        return pingSender;
    }

}
