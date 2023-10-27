package by.aurorasoft.signum.protocol.wialon.decoder;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;

public abstract class PackageDecoder {
    private final PackageDecoder nextDecoder;
    private final String packagePrefix;
    private final PackageDeserializer packageDeserializer;

    public PackageDecoder(PackageDecoder nextDecoder, String packagePrefix,
                          PackageDeserializer packageDeserializer) {
        this.nextDecoder = nextDecoder;
        this.packagePrefix = packagePrefix;
        this.packageDeserializer = packageDeserializer;
    }

    public final Package decode(String decoded) {
        if (this.canDecode(decoded)) {
            return this.doDecode(decoded);
        }
        return this.nextDecoder(decoded);
    }

    private boolean canDecode(String decoded) {
        //packagePrefix == null in starter and finisher decoding chain
        return this.packagePrefix != null && decoded.startsWith(this.packagePrefix);
    }

    private Package doDecode(String decoded) {
        return this.packageDeserializer.deserialize(decoded);
    }

    private Package nextDecoder(String decoded) {
        if (this.nextDecoder != null) {
            return this.nextDecoder.decode(decoded);
        }
        throw new NoSuitablePackageDecoderException();
    }
}
