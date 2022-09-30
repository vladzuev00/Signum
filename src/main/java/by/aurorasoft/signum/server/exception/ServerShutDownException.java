package by.aurorasoft.signum.server.exception;

public final class ServerShutDownException extends RuntimeException {
    public ServerShutDownException() {

    }

    public ServerShutDownException(String description) {
        super(description);
    }

    public ServerShutDownException(Exception cause) {
        super(cause);
    }

    public ServerShutDownException(String description, Exception cause) {
        super(description, cause);
    }
}
