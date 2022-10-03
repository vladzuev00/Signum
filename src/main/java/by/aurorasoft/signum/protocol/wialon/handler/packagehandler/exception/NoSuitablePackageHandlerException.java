package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.exception;

public final class NoSuitablePackageHandlerException extends RuntimeException {
    public NoSuitablePackageHandlerException() {

    }

    public NoSuitablePackageHandlerException(String description) {
        super(description);
    }

    public NoSuitablePackageHandlerException(Exception cause) {
        super(cause);
    }

    public NoSuitablePackageHandlerException(String description, Exception exception) {
        super(description, exception);
    }
}
