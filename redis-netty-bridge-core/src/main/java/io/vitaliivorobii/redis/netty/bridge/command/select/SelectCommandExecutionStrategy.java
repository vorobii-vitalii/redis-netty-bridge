package io.vitaliivorobii.redis.netty.bridge.command.select;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.resp.types.RespSimpleString;

public class SelectCommandExecutionStrategy implements CommandExecutionStrategy<Integer> {
    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, Integer args) {
        return true;
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, Integer dbIndex) {
        channelContext.attr(SelectedDatabase.INSTANCE).set(dbIndex);
        channelContext.writeAndFlush(new RespSimpleString("OK"));
    }
}
