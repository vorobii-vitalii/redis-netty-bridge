package io.vitaliivorobii.redis.netty.bridge.data;

import io.vitaliivorobii.redis.netty.bridge.command.get.RegexGetDataStrategy;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class RandomNumberGetDataStrategy extends RegexGetDataStrategy {

    public RandomNumberGetDataStrategy() {
        super(Pattern.compile("^RANDOM_(\\d+)$"));
    }

    @Override
    protected CompletableFuture<RespDataType> fetch(String key, String[] parsedArgs) {
        int bound = Integer.parseInt(parsedArgs[0]);
        return CompletableFuture.completedFuture(new RespBulkString(String.valueOf(new SecureRandom().nextInt(bound))));
    }
}
