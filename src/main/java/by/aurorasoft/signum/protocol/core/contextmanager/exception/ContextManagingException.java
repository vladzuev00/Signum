package by.aurorasoft.signum.protocol.core.contextmanager.exception;

public final class ContextManagingException extends RuntimeException {
    public ContextManagingException() {
        
    }

    public ContextManagingException(String description) {
        super(description);
    }

    public ContextManagingException(Exception cause) {
        super(cause);
    }

    public ContextManagingException(String description, Exception cause) {
        super(description, cause);
    }
}
