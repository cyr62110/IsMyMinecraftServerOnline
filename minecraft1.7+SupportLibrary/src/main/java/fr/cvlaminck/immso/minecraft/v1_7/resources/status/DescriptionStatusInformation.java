package fr.cvlaminck.immso.minecraft.v1_7.resources.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fr.cvlaminck.immso.minecraft.v1_7.resources.chat.ChatMessageResource;

/**
 * A little description of the server. This is a chat message as described in the API.
 *
 *
 * TODO : Not supported
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionStatusInformation
    extends ChatMessageResource {
}
