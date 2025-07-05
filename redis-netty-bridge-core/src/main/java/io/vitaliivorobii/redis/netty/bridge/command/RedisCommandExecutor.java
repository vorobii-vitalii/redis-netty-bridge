package io.vitaliivorobii.redis.netty.bridge.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public interface RedisCommandExecutor {
    void executeRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext);
}
