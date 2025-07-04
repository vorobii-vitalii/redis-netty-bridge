package io.vitaliivorobii.redis.netty.bridge.command.get;

import java.util.List;
import java.util.Optional;

import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.RespResponseWriter;
import io.vitaliivorobii.resp.types.RespNull;

public class DelegatingGetCommandStrategy implements GetCommandStrategy {
	private final List<GetCommandStrategy> getCommandStrategies;

	public DelegatingGetCommandStrategy(List<GetCommandStrategy> getCommandStrategies) {
		this.getCommandStrategies = getCommandStrategies;
	}

	@Override
	public Optional<RedisCommand> createIfApplicable(String key, RespResponseWriter respResponseWriter) {
		for (GetCommandStrategy strategy : getCommandStrategies) {
			Optional<RedisCommand> command = strategy.createIfApplicable(key, respResponseWriter);
			if (command.isPresent()) {
				return command;
			}
		}
		return Optional.of(new RedisCommand() {
			@Override
			public void execute() {
				respResponseWriter.reply(new RespNull());
			}
		});
	}
}
