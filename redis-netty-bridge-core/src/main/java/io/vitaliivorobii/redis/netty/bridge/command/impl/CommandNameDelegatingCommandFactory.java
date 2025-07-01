package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

import java.util.Map;

public class CommandNameDelegatingCommandFactory implements RedisCommandFactory {
    private final Map<String, RedisCommandFactory> factoryByCommandName;

    public CommandNameDelegatingCommandFactory(Map<String, RedisCommandFactory> factoryByCommandName) {
        this.factoryByCommandName = factoryByCommandName;
    }

    @Override
    public RedisCommand createRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext) {
        RedisCommandFactory factory = factoryByCommandName.get(clientRequest.commandName());
        if (factory != null) {
            return factory.createRedisCommand(clientRequest, channelContext);
        }
        return new ErrorCommand(channelContext, "Command unrecognized");
    }
}
