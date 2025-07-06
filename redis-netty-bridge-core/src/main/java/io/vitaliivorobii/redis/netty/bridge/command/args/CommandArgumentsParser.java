package io.vitaliivorobii.redis.netty.bridge.command.args;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;

public interface CommandArgumentsParser<A> {
    Either<A, String> parse(ClientRequest request);
}
