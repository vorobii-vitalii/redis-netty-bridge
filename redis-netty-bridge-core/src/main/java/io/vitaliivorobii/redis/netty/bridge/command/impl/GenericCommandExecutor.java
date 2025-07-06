package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespSimpleError;

import java.util.Collections;
import java.util.List;

public class GenericCommandExecutor<A> implements RedisCommandExecutor {
    private final CommandArgumentsParser<A> argumentsParser;
    private final List<CommandExecutionStrategy<A>> strategies;

    public GenericCommandExecutor(
            CommandArgumentsParser<A> argumentsParser,
            CommandExecutionStrategy<A> strategy
    ) {
        this(argumentsParser, Collections.singletonList(strategy));
    }

    public GenericCommandExecutor(
            CommandArgumentsParser<A> argumentsParser,
            List<CommandExecutionStrategy<A>> strategies
    ) {
        this.argumentsParser = argumentsParser;
        this.strategies = strategies;
    }

    @Override
    public void executeRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext) {
        Either<A, String> parseResult = argumentsParser.parse(clientRequest);
        if (parseResult.isLeft()) {
            A arguments = parseResult.getLeft();
            strategies.stream()
                    .filter(v -> v.canHandle(channelContext, arguments))
                    .findFirst()
                    .ifPresentOrElse(
                            strategy -> strategy.execute(channelContext, arguments),
                            () -> channelContext.writeAndFlush(
                                    new RespSimpleError("No strategy found for the command")
                            ));
        } else {
            channelContext.writeAndFlush(new RespSimpleError(parseResult.get()));
        }
    }

}
