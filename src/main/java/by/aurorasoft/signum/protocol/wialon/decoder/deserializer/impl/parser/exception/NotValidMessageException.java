package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception;

public final class NotValidMessageException extends RuntimeException {
    public NotValidMessageException() {

    }

    public NotValidMessageException(String description) {
        super(description);
    }

    public NotValidMessageException(Exception cause) {
        super(cause);
    }

    public NotValidMessageException(String description, Exception cause) {
        super(description, cause);
    }
}
