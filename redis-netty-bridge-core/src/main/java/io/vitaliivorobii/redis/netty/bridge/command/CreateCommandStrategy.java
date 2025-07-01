package io.vitaliivorobii.redis.netty.bridge.command;

import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public interface CreateCommandStrategy<A> {
    Optional<RedisCommand> createIfApplicable(ChannelHandlerContext channelContext, A arguments);
}
