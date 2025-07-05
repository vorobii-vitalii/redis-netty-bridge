package io.vitaliivorobii.redis.netty.bridge.command.ping;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.AtMostOneArgsParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

import java.util.List;
import java.util.Optional;

public class PingCommandExecutor extends GenericCommandExecutor<Optional<String>> {

    public PingCommandExecutor() {
        super(new AtMostOneArgsParser(), List.of(new PingCommandExecutionStrategy()));
    }
}
