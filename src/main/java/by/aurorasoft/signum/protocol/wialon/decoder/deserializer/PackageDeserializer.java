package by.aurorasoft.signum.protocol.wialon.decoder.deserializer;

import by.aurorasoft.signum.protocol.wialon.model.Package;

@FunctionalInterface
public interface PackageDeserializer {
    Package deserialize(String deserialized);

    static String removePrefix(String source, String prefix) {
        final int indexAfterPrefix = prefix.length();
        return source.substring(indexAfterPrefix);
    }
}
