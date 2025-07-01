package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.resp.types.RespSimpleError;

public class ErrorCommand implements RedisCommand {
    private final ChannelHandlerContext context;
    private final String errorMessage;

    public ErrorCommand(ChannelHandlerContext context, String errorMessage) {
        this.context = context;
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute() {
        context.writeAndFlush(new RespSimpleError(errorMessage));
    }
}
