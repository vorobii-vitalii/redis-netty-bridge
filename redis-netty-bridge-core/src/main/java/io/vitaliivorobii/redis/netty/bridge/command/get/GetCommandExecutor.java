package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.StringArgParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

import java.util.List;

public class GetCommandExecutor extends GenericCommandExecutor<String> {

    public GetCommandExecutor(GetDataStrategy getDataStrategy) {
        super(new StringArgParser(), List.of(new GETCommandExecutionStrategy(getDataStrategy)));
    }
}
