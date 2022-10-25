package by.aurorasoft.signum.protocol.wialon.service.sendcommand.exception;

public final class SendingCommandException extends RuntimeException {
    public SendingCommandException() {

    }

    public SendingCommandException(String description) {
        super(description);
    }

    public SendingCommandException(Exception cause) {
        super(cause);
    }

    public SendingCommandException(String description, Exception cause) {
        super(description, cause);
    }
}
