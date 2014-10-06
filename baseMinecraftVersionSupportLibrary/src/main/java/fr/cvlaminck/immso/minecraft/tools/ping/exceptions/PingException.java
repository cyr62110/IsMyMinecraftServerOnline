package fr.cvlaminck.immso.minecraft.tools.ping.exceptions;

/**
 *
 */
public abstract class PingException
        extends Exception {

    public PingException() {
    }

    public PingException(String message) {
        super(message);
    }

    public PingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PingException(Throwable cause) {
        super(cause);
    }

    public PingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
