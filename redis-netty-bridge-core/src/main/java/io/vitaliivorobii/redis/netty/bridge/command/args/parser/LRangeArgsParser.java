package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.command.args.types.LRangeArgs;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

import java.util.List;

public class LRangeArgsParser implements CommandArgumentsParser<LRangeArgs> {
    @Override
    public Either<LRangeArgs, String> parse(ClientRequest request) {
        List<String> args = request.arguments();
        int numArgs = args.size();
        if (numArgs != 3) {
            return Either.right("Expected 3 args - key, start, stop");
        }
        int start;
        try {
            start = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            return Either.right("Invalid argument format for start");
        }
        int end;
        try {
            end = Integer.parseInt(args.get(2));
        } catch (NumberFormatException e) {
            return Either.right("Invalid argument format for end");
        }
        return Either.left(new LRangeArgs(args.get(0), start, end));
    }
}
