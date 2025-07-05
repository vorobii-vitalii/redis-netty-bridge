package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class IntegerArgParser implements CommandArgumentsParser<Integer> {

    @Override
    public Either<Integer, String> parse(ClientRequest clientRequest) {
        if (clientRequest.arguments().size() != 1) {
            return Either.right("Expected exactly one argument");
        }
        try {
            return Either.left(Integer.parseInt(clientRequest.arguments().getFirst()));
        } catch (NumberFormatException error) {
            return Either.right("Parameter is not an integer");
        }
    }

}
