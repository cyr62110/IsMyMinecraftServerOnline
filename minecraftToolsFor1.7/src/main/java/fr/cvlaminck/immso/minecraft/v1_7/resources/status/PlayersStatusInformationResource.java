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
package fr.cvlaminck.immso.minecraft.v1_7.resources.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Information about how many players are playing, can play, etc...
 * Serialized using JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayersStatusInformationResource {

    /**
     * Maximum number of player that can play at a time on
     * the server.
     */
    private int max;

    /**
     * Count of actually online players.
     */
    private int online;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }
}
