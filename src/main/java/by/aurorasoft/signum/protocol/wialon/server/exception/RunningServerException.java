package by.aurorasoft.signum.protocol.wialon.server.exception;

public final class RunningServerException extends RuntimeException {
    public RunningServerException() {

    }

    public RunningServerException(String description) {
        super(description);
    }

    public RunningServerException(Exception cause) {
        super(cause);
    }

    public RunningServerException(String description, Exception cause) {
        super(description, cause);
    }
}
