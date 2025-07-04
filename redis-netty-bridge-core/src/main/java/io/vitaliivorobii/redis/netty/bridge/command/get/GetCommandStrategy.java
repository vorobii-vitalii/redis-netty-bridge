package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RespResponseWriter;

import java.util.Optional;

public interface GetCommandStrategy {

    Optional<RedisCommand> createIfApplicable(String key, RespResponseWriter respResponseWriter);

}
