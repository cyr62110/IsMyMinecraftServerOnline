package fr.cvlaminck.immso.minecraft.tools.ping.exceptions;

import fr.cvlaminck.immso.minecraft.MinecraftServer;

/**
 * Thrown by the PingSender implementation if an unhandled error occurred while pinging
 * the server.
 */
public class InternalErrorPingException extends PingException {
    private final static String MESSAGE = "An unhandled error occurred while pinging server at address %s:%d";

    public InternalErrorPingException(MinecraftServer server, Throwable cause) {
        super(String.format(MESSAGE, server.getHost(), server.getPort()), cause);
    }
}
