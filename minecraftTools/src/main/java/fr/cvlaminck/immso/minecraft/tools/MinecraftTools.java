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
package fr.cvlaminck.immso.minecraft.tools;

import fr.cvlaminck.immso.minecraft.MinecraftToolsVersion;
import fr.cvlaminck.immso.minecraft.tools.ping.PingSender;

/**
 * Collection of tool to interact with a minecraft server.
 * All tools may not be supported by all version of minecraft.
 * /!\ Methods providing tool instance may instantiate one instance of the tool class
 * per call or used a singleton to share the instance.
 */
public abstract class MinecraftTools {

    private MinecraftToolsVersion toolsVersion;

    protected MinecraftTools(MinecraftToolsVersion toolsVersion) {
        this.toolsVersion = toolsVersion;
    }

    /**
     * Tool that can ping server and obtain status information
     */
    public abstract PingSender pingSender();

    public MinecraftToolsVersion getToolsVersion() {
        return toolsVersion;
    }
}
