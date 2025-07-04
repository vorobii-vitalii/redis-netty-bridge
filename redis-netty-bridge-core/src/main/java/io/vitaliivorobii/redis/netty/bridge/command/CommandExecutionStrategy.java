package io.vitaliivorobii.redis.netty.bridge.command;

import io.netty.channel.ChannelHandlerContext;

public interface CommandExecutionStrategy<A> {
    boolean canHandle(ChannelHandlerContext channelHandlerContext, A args);

    void execute(ChannelHandlerContext channelContext, A arguments);
}
