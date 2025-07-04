package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vitalii.reply.RespResponseWriter;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;

public abstract class RegexGetCommandStrategy implements GetCommandStrategy {
	private final Pattern pattern;

	protected RegexGetCommandStrategy(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public Optional<RedisCommand> createIfApplicable(String key, RespResponseWriter respResponseWriter) {
		Matcher matcher = this.pattern.matcher(key);
		if (!matcher.matches()) {
			return Optional.empty();
		} else {
			int numVariables = matcher.groupCount();
			String[] parsedArgs = new String[numVariables];

			for (int i = 0; i < numVariables; ++i) {
				parsedArgs[i] = matcher.group(i + 1);
			}

			return Optional.of(this.createCommand(respResponseWriter, parsedArgs));
		}
	}

	public abstract RedisCommand createCommand(RespResponseWriter respResponseWriter, String[] args);

}
