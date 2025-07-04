package io.vitaliivorobii.redis.netty.bridge.command.test;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class IntegerArgParser implements CommandArgumentsParser<Integer> {
	@Override
	public Either<Integer, String> parse(ClientRequest clientRequest) {
		if (clientRequest.arguments().isEmpty()) {
			return Either.right("No arguments provided");
		}
		try {
			return Either.left(Integer.parseInt(clientRequest.arguments().getFirst()));
		}
		catch (NumberFormatException error) {
			return Either.right(error.getMessage());
		}
	}
}
