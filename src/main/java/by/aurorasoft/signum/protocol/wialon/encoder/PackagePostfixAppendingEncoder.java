package by.aurorasoft.signum.protocol.wialon.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.Package.PACKAGE_POSTFIX;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public final class PackagePostfixAppendingEncoder extends MessageToByteEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String source, ByteBuf byteBuf) {
        final String encoding = source + PACKAGE_POSTFIX;
        byteBuf.writeCharSequence(encoding, UTF_8);
    }
}
