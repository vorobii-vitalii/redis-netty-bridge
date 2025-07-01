package io.vitaliivorobii.redis.netty.bridge.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public interface RedisCommandFactory {
    RedisCommand createRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext);
}
