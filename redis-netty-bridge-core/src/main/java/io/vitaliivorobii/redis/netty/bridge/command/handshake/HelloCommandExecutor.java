package io.vitaliivorobii.redis.netty.bridge.command.handshake;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespInteger;
import io.vitaliivorobii.resp.types.RespMap;

import java.util.List;
import java.util.Map;

public class HelloCommandExecutor implements RedisCommandExecutor {

    @Override
    public void executeRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.writeAndFlush(new RespMap(Map.of(
                new RespBulkString("server"), new RespBulkString("redis"),
                new RespBulkString("version"), new RespBulkString("6.0.0"),
                new RespBulkString("proto"), new RespInteger(3),
                new RespBulkString("id"), new RespInteger(10),
                new RespBulkString("mode"), new RespBulkString("standalone"),
                new RespBulkString("role"), new RespBulkString("master"),
                new RespBulkString("modules"), new RespArray(List.of())
        )));
    }

}
