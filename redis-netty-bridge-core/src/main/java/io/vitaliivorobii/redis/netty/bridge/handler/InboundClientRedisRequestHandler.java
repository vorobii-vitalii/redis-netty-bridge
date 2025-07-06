package io.vitaliivorobii.redis.netty.bridge.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

@ChannelHandler.Sharable
public class InboundClientRedisRequestHandler extends SimpleChannelInboundHandler<ClientRequest> {
    private final RedisCommandExecutor redisCommandExecutor;

    public InboundClientRedisRequestHandler(RedisCommandExecutor redisCommandExecutor) {
        this.redisCommandExecutor = redisCommandExecutor;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext context, ClientRequest clientRequest) {
        redisCommandExecutor.executeRedisCommand(clientRequest, context);
    }

}
