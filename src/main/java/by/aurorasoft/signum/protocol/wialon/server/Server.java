package by.aurorasoft.signum.protocol.wialon.server;

import by.aurorasoft.signum.config.property.ServerProperty;
import by.aurorasoft.signum.protocol.wialon.decoder.WialonDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.impl.StarterPackageDecoder;
import by.aurorasoft.signum.protocol.wialon.encoder.PackagePostfixAppendingEncoder;
import by.aurorasoft.signum.protocol.wialon.handler.ExceptionHandler;
import by.aurorasoft.signum.protocol.wialon.handler.WialonHandler;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.StarterPackageHandler;
import by.aurorasoft.signum.protocol.wialon.server.exception.RunningServerException;
import by.aurorasoft.signum.protocol.wialon.server.exception.ServerShutDownException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Slf4j
public final class Server implements AutoCloseable {
    private final StarterPackageDecoder starterPackageDecoder;
    private final StarterPackageHandler starterPackageHandler;
    private final ContextManager contextWorker;
    private final int timeoutSeconds;
    private final EventLoopGroup connectionLoopGroup;
    private final EventLoopGroup dataProcessLoopGroup;
    private final int port;

    public Server(StarterPackageDecoder starterPackageDecoder, StarterPackageHandler starterPackageHandler,
                  ContextManager contextWorker, ServerProperty serverConfiguration) {
        this.starterPackageDecoder = starterPackageDecoder;
        this.starterPackageHandler = starterPackageHandler;
        this.contextWorker = contextWorker;
        this.timeoutSeconds = serverConfiguration.getTimeoutSeconds();
        this.connectionLoopGroup = new NioEventLoopGroup(serverConfiguration.getConnectionThreads());
        this.dataProcessLoopGroup = new NioEventLoopGroup(serverConfiguration.getDataProcessThreads());
        this.port = serverConfiguration.getPort();
    }

    public void run() {
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.connectionLoopGroup, this.dataProcessLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    Server.this.createRequestDecoder(),
                                    Server.this.createReadTimeoutHandler(),
                                    Server.this.createResponseEncoder(),
                                    Server.this.createRequestHandler(),
                                    Server.this.createExceptionHandler());
                        }
                    });
            final ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (final InterruptedException cause) {
            throw new RunningServerException(cause);
        }
    }

    @PreDestroy
    @Override
    public void close() {
        try {
            this.connectionLoopGroup.shutdownGracefully().sync();
            this.dataProcessLoopGroup.shutdownGracefully().sync();
        } catch (final InterruptedException cause) {
            throw new ServerShutDownException(cause);
        }
    }

    private WialonDecoder createRequestDecoder() {
        return new WialonDecoder(this.starterPackageDecoder);
    }

    private ReadTimeoutHandler createReadTimeoutHandler() {
        return new ReadTimeoutHandler(this.timeoutSeconds, SECONDS);
    }

    private WialonHandler createRequestHandler() {
        return new WialonHandler(this.starterPackageHandler, this.contextWorker);
    }

    private ExceptionHandler createExceptionHandler() {
        return new ExceptionHandler(this.contextWorker);
    }

    private PackagePostfixAppendingEncoder createResponseEncoder() {
        return new PackagePostfixAppendingEncoder();
    }
}
