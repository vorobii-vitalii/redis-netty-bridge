package io.vitaliivorobii.redis.netty.bridge.command.role;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespInteger;

import java.util.List;

public class RoleCommandExecutionStrategy implements CommandExecutionStrategy<Void> {

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, Void ignored) {
        return true;
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, Void ignored) {
        channelContext.writeAndFlush(new RespArray(List.of(
                new RespBulkString("master"),
                new RespInteger(123),
                new RespArray(List.of())
        )));
    }
}
