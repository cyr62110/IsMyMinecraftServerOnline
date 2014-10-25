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

import fr.cvlaminck.immso.minecraft.v1_7.resources.chat.ChatMessageResource;

/**
 * A little description of the server. This is a chat message as described in the API.
 * <p/>
 * <p/>
 * TODO : Not supported
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionStatusInformation
        extends ChatMessageResource {
}
