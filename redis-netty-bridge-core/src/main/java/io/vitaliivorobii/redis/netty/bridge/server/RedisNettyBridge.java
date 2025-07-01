package io.vitaliivorobii.redis.netty.bridge.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.decoder.ClientRequestDecoder;
import io.vitaliivorobii.redis.netty.bridge.handler.InboundClientRedisRequestHandler;
import io.vitaliivorobii.resp.decoder.ByteToRespMessageDecoder;
import io.vitaliivorobii.resp.decoder.DefaultRespDecoder;
import io.vitaliivorobii.resp.encoder.DefaultRespEncoder;
import io.vitaliivorobii.resp.encoder.RespMessageToByteEncoder;

import java.net.InetSocketAddress;

public class RedisNettyBridge {
    private final int port;
    private final RedisCommandFactory redisCommandFactory;

    public RedisNettyBridge(int port, RedisCommandFactory redisCommandFactory) {
        this.port = port;
        this.redisCommandFactory = redisCommandFactory;
    }

    public ChannelFuture start() throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ByteToRespMessageDecoder(
                                new DefaultRespDecoder()
                        ));
                        ch.pipeline().addLast(new RespMessageToByteEncoder(
                                new DefaultRespEncoder()
                        ));
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        ch.pipeline().addLast(new ClientRequestDecoder());
                        ch.pipeline().addLast(new InboundClientRedisRequestHandler(redisCommandFactory));
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        return channelFuture.channel().closeFuture();
    }
}
