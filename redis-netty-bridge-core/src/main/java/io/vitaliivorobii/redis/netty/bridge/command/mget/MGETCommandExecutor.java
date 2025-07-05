package io.vitaliivorobii.redis.netty.bridge.command.mget;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.ArrayArgsParser;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

import java.util.List;

public class MGETCommandExecutor extends GenericCommandExecutor<List<String>> {

    public MGETCommandExecutor(GetDataStrategy getDataStrategy) {
        super(new ArrayArgsParser(), new MGETCommandExecutionStrategy(getDataStrategy));
    }
}
