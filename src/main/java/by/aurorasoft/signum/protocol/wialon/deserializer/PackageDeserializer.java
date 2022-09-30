package by.aurorasoft.signum.protocol.wialon.deserializer;

import by.aurorasoft.signum.protocol.wialon.model.Package;

import static by.aurorasoft.signum.protocol.wialon.model.Package.PACKAGE_POSTFIX;

@FunctionalInterface
public interface PackageDeserializer {
    Package deserialize(String deserialized);

    static String removePrefixAndPostfix(String source, String prefix) {
        final int indexPrefixEnd = prefix.length() - 1;
        final int indexPostfixStart = source.length() - PACKAGE_POSTFIX.length();
        return source.substring(indexPrefixEnd + 1, indexPostfixStart);
    }
}
