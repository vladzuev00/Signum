package by.wialontransport.protocol.wialon.decoder.impl;

import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

@Component
public final class StarterPackageDecoder extends PackageDecoder {

    public StarterPackageDecoder(LoginPackageDecoder nextPackageDecoder) {
        super(nextPackageDecoder, null, null);
    }
}
