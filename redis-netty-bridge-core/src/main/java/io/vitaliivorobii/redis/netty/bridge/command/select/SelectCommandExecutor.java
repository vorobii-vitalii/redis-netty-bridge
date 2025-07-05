package io.vitaliivorobii.redis.netty.bridge.command.select;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.IntegerArgParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

import java.util.List;

public class SelectCommandExecutor extends GenericCommandExecutor<Integer> {
    public SelectCommandExecutor() {
        super(new IntegerArgParser(), List.of(new SelectCommandExecutionStrategy()));
    }
}
