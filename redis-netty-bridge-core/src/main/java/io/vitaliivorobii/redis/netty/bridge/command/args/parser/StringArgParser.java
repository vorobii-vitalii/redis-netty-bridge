package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

import java.util.List;

public class StringArgParser implements CommandArgumentsParser<String> {

    @Override
    public Either<String, String> parse(ClientRequest request) {
        List<String> arguments = request.arguments();
        if (arguments.isEmpty()) {
            return Either.right("No key specified!");
        }
        String key = arguments.getFirst();
        return Either.left(key);
    }

}
