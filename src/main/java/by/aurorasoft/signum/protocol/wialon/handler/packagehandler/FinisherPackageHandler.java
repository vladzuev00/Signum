package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class FinisherPackageHandler extends PackageHandler {
    public FinisherPackageHandler() {
        super(null, null);
    }

    @Override
    protected Optional<String> doHandle(Package requestPackage, ChannelHandlerContext context) {
        throw new UnsupportedOperationException();
    }
}
