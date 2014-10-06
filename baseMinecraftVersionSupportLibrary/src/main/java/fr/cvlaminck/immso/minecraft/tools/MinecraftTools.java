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
