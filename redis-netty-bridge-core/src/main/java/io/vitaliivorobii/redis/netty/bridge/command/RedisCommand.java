package io.vitaliivorobii.redis.netty.bridge.command;

import java.util.concurrent.CompletionStage;

public interface RedisCommand {
    CompletionStage<?> execute();
}
