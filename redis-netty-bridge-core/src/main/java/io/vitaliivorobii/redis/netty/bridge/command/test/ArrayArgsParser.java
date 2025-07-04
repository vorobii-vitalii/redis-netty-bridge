package io.vitaliivorobii.redis.netty.bridge.command.test;

import java.util.List;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class ArrayArgsParser implements CommandArgumentsParser<List<String>> {
	@Override
	public Either<List<String>, String> parse(ClientRequest clientRequest) {
		return Either.left(clientRequest.arguments());
	}
}
