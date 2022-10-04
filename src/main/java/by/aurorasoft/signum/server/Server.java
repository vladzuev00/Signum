package by.aurorasoft.signum.server;

import by.aurorasoft.signum.config.property.ServerProperty;
import by.aurorasoft.signum.protocol.wialon.decoder.WialonDecoder;
import by.aurorasoft.signum.protocol.wialon.handler.RequestHandler;
import by.aurorasoft.signum.server.exception.RunningServerException;
import by.aurorasoft.signum.server.exception.ServerShutDownException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Slf4j
public final class Server implements AutoCloseable {
    private final WialonDecoder requestDecoder;
    private final MessageToByteEncoder<String> responseEncoder;
    private final ReadTimeoutHandler readTimeoutHandler;
    private final RequestHandler requestHandler;
    private final EventLoopGroup connectionLoopGroup;
    private final EventLoopGroup dataProcessLoopGroup;
    private final int port;

    public Server(WialonDecoder requestDecoder, MessageToByteEncoder<String> responseEncoder,
                  RequestHandler requestHandler, ServerProperty serverConfiguration) {
        this.requestDecoder = requestDecoder;
        this.responseEncoder = responseEncoder;
        this.readTimeoutHandler = new ReadTimeoutHandler(serverConfiguration.getTimeoutSeconds(), SECONDS);
        this.requestHandler = requestHandler;
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
                                    Server.this.requestDecoder,
                                    Server.this.readTimeoutHandler,
                                    Server.this.responseEncoder,
                                    Server.this.requestHandler);
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
}
