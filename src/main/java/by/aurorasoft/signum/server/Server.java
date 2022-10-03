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
    private final RequestDecoder requestDecoder;
    private final MessageToByteEncoder<String> responseEncoder;
    private final RequestHandler requestHandler;
    private final EventLoopGroup connectionLoopGroup;
    private final EventLoopGroup dataProcessLoopGroup;

    public Server(RequestDecoder requestDecoder,
                  MessageToByteEncoder<String> responseEncoder,
                  RequestHandler requestHandler,
                  @Value("${netty.server.eventLoopGroup.connection.threads}") int amountOfThreadOfConnectionLoopGroup,
                  @Value("${netty.server.eventLoopGroup.dataProcess.threads}") int amountOfThreadsOfDataProcessLoopGroup) {
        this.requestDecoder = requestDecoder;
        this.responseEncoder = responseEncoder;
        this.requestHandler = requestHandler;
        this.connectionLoopGroup = new NioEventLoopGroup(amountOfThreadOfConnectionLoopGroup);
        this.dataProcessLoopGroup = new NioEventLoopGroup(amountOfThreadsOfDataProcessLoopGroup);
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
