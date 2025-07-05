package io.vitaliivorobii.redis.netty.bridge.command.select;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.IntegerArgParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

public class SelectCommandExecutor extends GenericCommandExecutor<Integer> {
    public SelectCommandExecutor() {
        super(new IntegerArgParser(), new SelectCommandExecutionStrategy());
    }
}
