package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

@Component
public final class StarterPackageDecoder extends PackageDecoder {

    public StarterPackageDecoder(LoginPackageDecoder nextPackageDecoder) {
        super(nextPackageDecoder, null, null);
    }
}
