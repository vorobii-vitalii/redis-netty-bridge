package io.vitaliivorobii.redis.netty.bridge.command.ping;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespSimpleString;

import java.util.Optional;

public class PingCommandExecutionStrategy implements CommandExecutionStrategy<Optional<String>> {

    @Override
    public boolean canHandle(ChannelHandlerContext channelHandlerContext, Optional<String> args) {
        return true;
    }

    @Override
    public void execute(ChannelHandlerContext channelContext, Optional<String> args) {
        RespDataType reply =
                args.<RespDataType>map(RespBulkString::new).orElseGet(() -> new RespSimpleString("PONG"));
        channelContext.writeAndFlush(reply);
    }
}
