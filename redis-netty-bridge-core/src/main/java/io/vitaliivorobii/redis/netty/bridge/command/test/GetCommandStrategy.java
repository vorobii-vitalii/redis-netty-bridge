package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.util.Optional;

import io.vitalii.reply.RespResponseWriter;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;

public interface GetCommandStrategy {

	Optional<RedisCommand> createIfApplicable(String key, RespResponseWriter respResponseWriter);

}
