package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.command.CreateCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.command.common.ErrorCommand;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

import java.util.List;
import java.util.function.Function;

public class GenericCommandFactory<A> implements RedisCommandFactory {
    private final CommandArgumentsParser<A> argumentsParser;
    private final List<CreateCommandStrategy<A>> strategies;
    private final Function<ChannelHandlerContext, RedisCommand> defaultCommandSupplier;

    public GenericCommandFactory(
            CommandArgumentsParser<A> argumentsParser,
            List<CreateCommandStrategy<A>> strategies,
            Function<ChannelHandlerContext, RedisCommand> defaultCommandSupplier
    ) {
        this.argumentsParser = argumentsParser;
        this.strategies = strategies;
        this.defaultCommandSupplier = defaultCommandSupplier;
    }

    @Override
    public RedisCommand createRedisCommand(ClientRequest clientRequest, ChannelHandlerContext channelContext) {
        Either<A, String> parseResult = argumentsParser.parse(clientRequest);
        if (parseResult.isLeft()) {
            A arguments = parseResult.getLeft();
            return strategies.stream()
                    .flatMap(v -> v.createIfApplicable(channelContext, arguments).stream())
                    .findFirst()
                    .orElseGet(() -> defaultCommandSupplier.apply(channelContext));
        }
        return new ErrorCommand(channelContext, parseResult.get());
    }

}
