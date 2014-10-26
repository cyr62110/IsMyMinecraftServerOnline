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
package fr.cvlaminck.immso.minecraft;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.cvlaminck.immso.minecraft.tools.MinecraftTools;

/**
 * Register containing all tools version that can be used to communicate with
 * a Minecraft server.
 */
public class SupportedToolsVersions {

    private static SupportedToolsVersions instance = null;

    private Map<MinecraftToolsVersion, MinecraftTools> tools = null;

    public SupportedToolsVersions() {
        tools = new HashMap<>();
    }

    public static SupportedToolsVersions getInstance() {
        if (instance == null) {
            synchronized (SupportedToolsVersions.class) {
                if (instance == null)
                    instance = new SupportedToolsVersions();
            }
        }
        return instance;
    }

    public void registerTools(MinecraftTools tools) {
        this.tools.put(tools.getToolsVersion(), tools);
    }

    public Collection<MinecraftToolsVersion> getAvailableVersions() {
        return this.tools.keySet();
    }

    public MinecraftTools get(MinecraftToolsVersion version) {
        return this.tools.get(version);
    }

    public MinecraftTools get(String toolsVersion) {
        return this.get(new MinecraftToolsVersion(toolsVersion));
    }

}
