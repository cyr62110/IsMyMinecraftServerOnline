package fr.cvlaminck.immso.minecraft;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        if(instance == null) {
            synchronized (SupportedToolsVersions.class) {
                if(instance == null)
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
