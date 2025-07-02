package io.vitaliivorobii.redis.netty.bridge.command.impl;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.CreateCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexBasedCreateCommandStrategy<A> implements CreateCommandStrategy<A> {
    private final Function<A, String> textExtractor;
    private final Pattern pattern;

    protected RegexBasedCreateCommandStrategy(Function<A, String> textExtractor, Pattern pattern) {
        this.textExtractor = textExtractor;
        this.pattern = pattern;
    }

    @Override
    public Optional<RedisCommand> createIfApplicable(ChannelHandlerContext channelContext, A arguments) {
        String text = textExtractor.apply(arguments);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        int numVariables = matcher.groupCount();
        String[] parsedArgs = new String[numVariables];
        for (int i = 0; i < numVariables; i++) {
            parsedArgs[i] = matcher.group(i + 1);
        }
        return Optional.of(createCommand(channelContext, parsedArgs, arguments));
    }

    public abstract RedisCommand createCommand(ChannelHandlerContext context, String[] textArgs, A arguments);
}
