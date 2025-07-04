package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.util.Optional;

import io.netty.channel.ChannelHandlerContext;
import io.vitalii.command.SelectRedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.CreateCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;

public class SelectCommandStrategy implements CreateCommandStrategy<Integer> {

	@Override
	public Optional<RedisCommand> createIfApplicable(ChannelHandlerContext channelHandlerContext, Integer port) {
		return Optional.of(new SelectRedisCommand(channelHandlerContext, port));
	}
}
