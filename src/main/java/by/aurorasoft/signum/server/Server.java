package by.aurorasoft.signum.server;

import by.aurorasoft.signum.protocol.wialon.RequestDecoder;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
@Slf4j
@PropertySource("classpath:server.properties")
public final class Server implements AutoCloseable {

    @Value("${netty.server.port}")
    private int port;

    @Value("${netty.server.eventLoopGroup.connection.threads}")
    private int amountOfThreadOfConnectionLoopGroup;

    @Value("${netty.server.eventLoopGroup.dataProcess.threads}")
    private int amountOfThreadsOfDataProcessLoopGroup;

    private EventLoopGroup connectionLoopGroup;
    private EventLoopGroup dataProcessLoopGroup;
    private final RequestDecoder requestDecoder;
    private final MessageToByteEncoder<String> responseEncoder;
    private final RequestHandler requestHandler;

    public Server(RequestDecoder requestDecoder,
                  MessageToByteEncoder<String> responseEncoder,
                  RequestHandler requestHandler) {
        this.requestDecoder = requestDecoder;
        this.responseEncoder = responseEncoder;
        this.requestHandler = requestHandler;
    }

    public void run() {
        try {
            this.connectionLoopGroup = new NioEventLoopGroup(this.amountOfThreadOfConnectionLoopGroup);
            this.dataProcessLoopGroup = new NioEventLoopGroup(this.amountOfThreadsOfDataProcessLoopGroup);
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(this.connectionLoopGroup, this.dataProcessLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    Server.this.requestDecoder,
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
            if (this.connectionLoopGroup != null) {
                this.connectionLoopGroup.shutdownGracefully().sync();
            }
            if (this.dataProcessLoopGroup != null) {
                this.dataProcessLoopGroup.shutdownGracefully().sync();
            }
        } catch (final InterruptedException cause) {
            throw new ServerShutDownException(cause);
        }
    }
}
