package by.aurorasoft.signum.protocol.wialon.handler.exception;

public final class NoSuitablePackageAnswererException extends RuntimeException {
    public NoSuitablePackageAnswererException() {

    }

    public NoSuitablePackageAnswererException(String description) {
        super(description);
    }

    public NoSuitablePackageAnswererException(Exception cause) {
        super(cause);
    }

    public NoSuitablePackageAnswererException(String description, Exception exception) {
        super(description, exception);
    }
}
