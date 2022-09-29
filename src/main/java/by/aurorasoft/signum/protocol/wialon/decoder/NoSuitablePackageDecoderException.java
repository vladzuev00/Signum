package by.aurorasoft.signum.protocol.wialon.decoder;

public final class NoSuitablePackageDecoderException extends RuntimeException {
    public NoSuitablePackageDecoderException() {
        super();
    }

    public NoSuitablePackageDecoderException(String description) {
        super(description);
    }

    public NoSuitablePackageDecoderException(Exception cause) {
        super(cause);
    }

    public NoSuitablePackageDecoderException(String description, Exception cause) {
        super(description, cause);
    }
}
