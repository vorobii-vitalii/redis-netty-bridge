package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

import java.util.List;
import java.util.Optional;

public class AtMostOneArgsParser implements CommandArgumentsParser<Optional<String>> {
    @Override
    public Either<Optional<String>, String> parse(ClientRequest request) {
        List<String> args = request.arguments();
        if (args.size() > 1) {
            return Either.right("Expected at most one param, but was given " + args.size());
        }
        return Either.left(args.stream().findFirst());
    }
}
