package by.wialontransport.protocol.wialon;

import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import by.wialontransport.protocol.wialon.decoder.impl.StarterPackageDecoder;
import by.wialontransport.protocol.wialon.model.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class RequestDecoder extends ReplayingDecoder<Package> {
    private final PackageDecoder starterPackageDecoder;

    public RequestDecoder(StarterPackageDecoder starterPackageDecoder) {
        this.starterPackageDecoder = starterPackageDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> outObjects) {
        final int readableBytes = byteBuf.readableBytes();
        final byte[] bytes = new byte[readableBytes];
        byteBuf.readBytes(bytes);
        final String decoded = new String(bytes);
        final Package requestPackage = this.starterPackageDecoder.decode(decoded);
        outObjects.add(requestPackage);
    }
}
