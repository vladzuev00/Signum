package by.aurorasoft.signum.protocol.wialon.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import static by.aurorasoft.signum.protocol.wialon.model.Package.PACKAGE_POSTFIX;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public final class PackagePostfixAppendingEncoder extends MessageToByteEncoder<String> {
    private static final String TEMPLATE_MESSAGE_START_ENCODING
            = "Start encoding outbound package: '%s'.";

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String source, ByteBuf byteBuf) {
        log.info(format(TEMPLATE_MESSAGE_START_ENCODING, source));
        final String encoding = source + PACKAGE_POSTFIX;
        byteBuf.writeCharSequence(encoding, UTF_8);
    }
}
