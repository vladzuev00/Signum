package by.aurorasoft.signum.protocol.wialon.decoder.deserializer;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RequestPackageDeserializer {
    private final String packagePrefix;

    public final Package deserialize(final String source) {
        final String message = this.removePackagePrefix(source);
        return this.deserializeMessage(message);
    }

    protected abstract Package deserializeMessage(final String message);

    private String removePackagePrefix(final String source) {
        final int indexAfterPrefix = this.packagePrefix.length();
        return source.substring(indexAfterPrefix);
    }
}
