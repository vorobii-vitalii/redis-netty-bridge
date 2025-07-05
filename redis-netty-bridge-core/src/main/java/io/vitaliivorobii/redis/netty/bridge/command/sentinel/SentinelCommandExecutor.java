package io.vitaliivorobii.redis.netty.bridge.command.sentinel;

import io.vitaliivorobii.redis.netty.bridge.command.CommandExecutionStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.args.CommandArgumentsParser;
import io.vitaliivorobii.redis.netty.bridge.command.args.parser.ArrayArgsParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

import java.util.List;

public class SentinelCommandExecutor extends GenericCommandExecutor<List<String>> {

    public SentinelCommandExecutor(List<CommandExecutionStrategy<List<String>>> strategies) {
        super(new ArrayArgsParser(), strategies);
    }
}
