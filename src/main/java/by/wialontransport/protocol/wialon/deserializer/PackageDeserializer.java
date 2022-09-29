package by.wialontransport.protocol.wialon.deserializer;


import by.wialontransport.protocol.wialon.model.Package;

@FunctionalInterface
public interface PackageDeserializer {
    Package deserialize(String deserialized);
}
