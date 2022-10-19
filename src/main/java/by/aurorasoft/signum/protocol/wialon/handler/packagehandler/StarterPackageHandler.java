package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class StarterPackageHandler extends PackageHandler {
    public StarterPackageHandler(LoginPackageHandler nextHandler) {
        super(null, nextHandler);
    }

    @Override
    protected Optional<String> doHandle(Package requestPackage, ChannelHandlerContext context) {
        throw new UnsupportedOperationException();
    }
}
