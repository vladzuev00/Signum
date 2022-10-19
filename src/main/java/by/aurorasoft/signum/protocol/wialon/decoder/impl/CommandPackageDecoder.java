package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.CommandPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.PACKAGE_PREFIX;

@Component
public final class CommandPackageDecoder extends PackageDecoder {

    public CommandPackageDecoder(FinisherPackageDecoder nextDecoder, CommandPackageDeserializer packageDeserializer) {
        super(nextDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
