package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespSimpleError;

import java.util.Map;

public class CommandNameDelegatingCommandExecutor implements RedisCommandExecutor {
    private final Map<String, RedisCommandExecutor> factoryByCommandName;

    public CommandNameDelegatingCommandExecutor(Map<String, RedisCommandExecutor> factoryByCommandName) {
        this.factoryByCommandName = factoryByCommandName;
    }

    @Override
    public void executeRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext) {
        RedisCommandExecutor factory = factoryByCommandName.get(clientRequest.commandName());
        if (factory != null) {
            factory.executeRedisCommand(clientRequest, channelContext);
        } else {
            channelContext.writeAndFlush(new RespSimpleError("Command unrecognized"));
        }
    }
}
