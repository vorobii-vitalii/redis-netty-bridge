package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.resp.types.RespDataType;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexGetDataStrategy implements GetDataStrategy {
    private final Pattern pattern;

    protected RegexGetDataStrategy(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Optional<CompletableFuture<RespDataType>> getData(String key) {
        Matcher matcher = this.pattern.matcher(key);
        if (!matcher.matches()) {
            return Optional.empty();
        } else {
            int numVariables = matcher.groupCount();
            String[] parsedArgs = new String[numVariables];

            for (int i = 0; i < numVariables; ++i) {
                parsedArgs[i] = matcher.group(i + 1);
            }

            return Optional.of(fetch(key, parsedArgs));
        }
    }

    protected abstract CompletableFuture<RespDataType> fetch(String key, String[] parsedArgs);

}
