package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public class NoArgsExpectedParser implements CommandArgumentsParser<Void> {
    @Override
    public Either<Void, String> parse(ClientRequest request) {
        if (request.arguments().isEmpty()) {
            return Either.left(null);
        }
        return Either.right("No arguments expected");
    }
}
