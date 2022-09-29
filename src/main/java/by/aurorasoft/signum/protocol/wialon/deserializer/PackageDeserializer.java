package by.aurorasoft.signum.protocol.wialon.deserializer;


import by.aurorasoft.signum.protocol.wialon.model.Package;
import static by.aurorasoft.signum.protocol.wialon.model.Package.PACKAGE_POSTFIX;

@FunctionalInterface
public interface PackageDeserializer {
    Package deserialize(String deserialized);

    static String removePrefixAndPostfix(String source, String prefix){
        return source
                .replace(prefix, "")
                .replace(PACKAGE_POSTFIX, "");
    }
}
