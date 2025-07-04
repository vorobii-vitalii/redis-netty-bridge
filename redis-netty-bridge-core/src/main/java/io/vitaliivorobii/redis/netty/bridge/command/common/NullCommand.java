package io.vitaliivorobii.redis.netty.bridge.command.common;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.resp.types.RespNull;

public class NullCommand implements RedisCommand {
    private final ChannelHandlerContext context;

    public NullCommand(ChannelHandlerContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        context.writeAndFlush(new RespNull());
    }
}
