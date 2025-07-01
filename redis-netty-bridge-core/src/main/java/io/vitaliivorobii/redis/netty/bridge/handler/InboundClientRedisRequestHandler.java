package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class InboundClientRedisRequestHandler extends SimpleChannelInboundHandler<ClientRequest> {
    private final RedisCommandFactory redisCommandFactory;

    public InboundClientRedisRequestHandler(RedisCommandFactory redisCommandFactory) {
        this.redisCommandFactory = redisCommandFactory;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ClientRequest clientRequest) {
        redisCommandFactory.createRedisCommand(clientRequest, ctx)
                .execute();
    }

}
