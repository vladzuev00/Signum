package by.aurorasoft.signum.service.exception;

public final class NoSuchTrackerException extends RuntimeException {
    public NoSuchTrackerException() {

    }

    public NoSuchTrackerException(String description) {
        super(description);
    }

    public NoSuchTrackerException(Exception cause) {
        super(cause);
    }

    public NoSuchTrackerException(String description, Exception cause) {
        super(description, cause);
    }
}
