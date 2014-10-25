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

/**
 * Version of the tools
 */
public class MinecraftToolsVersion
        implements Comparable<MinecraftToolsVersion> {

    /**
     * String representing all version of Minecraft that are supported by this
     * version of the tools. ex. "1.7.x", "1.8.0", "13w16x - 13w26x"
     */
    private String supportedMinecraftVersions;

    /**
     * String representing this version of the tools. This value
     * is used internally.
     */
    private String toolsVersion;

    /**
     * When the first minecraft version supported by this version of the tools
     * has been released to the public.
     */
    private long minecraftReleaseDate;

    /*package*/ MinecraftToolsVersion(String toolsVersion) {
        this.toolsVersion = toolsVersion;
    }

    /**
     * TODO
     *
     * @deprecated
     */
    public MinecraftToolsVersion(String supportedMinecraftVersions, String toolsVersion) {
        this.supportedMinecraftVersions = supportedMinecraftVersions;
        this.toolsVersion = toolsVersion;
    }

    public MinecraftToolsVersion(String supportedMinecraftVersions, String toolsVersion, long minecraftReleaseDate) {
        this.supportedMinecraftVersions = supportedMinecraftVersions;
        this.toolsVersion = toolsVersion;
        this.minecraftReleaseDate = minecraftReleaseDate;
    }

    public String getSupportedMinecraftVersions() {
        return supportedMinecraftVersions;
    }

    public void setSupportedMinecraftVersions(String supportedMinecraftVersions) {
        this.supportedMinecraftVersions = supportedMinecraftVersions;
    }

    public String getToolsVersion() {
        return toolsVersion;
    }

    public void setToolsVersion(String toolsVersion) {
        this.toolsVersion = toolsVersion;
    }

    public long getMinecraftReleaseDate() {
        return minecraftReleaseDate;
    }

    public void setMinecraftReleaseDate(long minecraftReleaseDate) {
        this.minecraftReleaseDate = minecraftReleaseDate;
    }

    @Override
    public int compareTo(MinecraftToolsVersion o) {
        //TODO
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftToolsVersion)) return false;

        MinecraftToolsVersion that = (MinecraftToolsVersion) o;

        if (!toolsVersion.equals(that.toolsVersion)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = toolsVersion.hashCode();
        return result;
    }
}
