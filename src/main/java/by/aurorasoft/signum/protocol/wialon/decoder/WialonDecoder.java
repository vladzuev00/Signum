package by.aurorasoft.signum.protocol.wialon.decoder;

import by.aurorasoft.signum.protocol.wialon.decoder.impl.StarterPackageDecoder;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.IntStream.range;

@Slf4j
public final class WialonDecoder extends ReplayingDecoder<Package> {
    private static final String TEMPLATE_MESSAGE_START_DECODING_INBOUND_PACKAGE
            = "Start decoding inbound package: '%s'.";
    private static final char CHARACTER_OF_END_REQUEST_PACKAGE = '\n';

    private final PackageDecoder starterPackageDecoder;

    public WialonDecoder(StarterPackageDecoder starterPackageDecoder) {
        this.starterPackageDecoder = starterPackageDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> outObjects) {
        final String serializedPackage = findSerializedPackage(byteBuf);
        log.info(format(TEMPLATE_MESSAGE_START_DECODING_INBOUND_PACKAGE, serializedPackage));
        final Package requestPackage = this.starterPackageDecoder.decode(serializedPackage);
        outObjects.add(requestPackage);
    }

    private static String findSerializedPackage(ByteBuf byteBuf) {
        final StringBuilder requestPackageBuilder = new StringBuilder();
        char currentAppendedCharacter;
        do {
            currentAppendedCharacter = (char) byteBuf.readByte();
            requestPackageBuilder.append(currentAppendedCharacter);
        } while (byteBuf.isReadable() && currentAppendedCharacter != CHARACTER_OF_END_REQUEST_PACKAGE);
        removePostfix(requestPackageBuilder);
        return requestPackageBuilder.toString();
    }

    private static void removePostfix(StringBuilder requestPackageBuilder) {
        range(0, Package.PACKAGE_POSTFIX.length())
                .forEach(i -> requestPackageBuilder.deleteCharAt(requestPackageBuilder.length() - 1));
    }
}
