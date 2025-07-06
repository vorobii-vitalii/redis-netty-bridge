package io.vitaliivorobii.redis.netty.bridge.command.role;

import io.vitaliivorobii.redis.netty.bridge.command.args.parser.NoArgsExpectedParser;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandExecutor;

public class RoleCommandExecutor extends GenericCommandExecutor<Void> {
    public RoleCommandExecutor() {
        super(new NoArgsExpectedParser(), new RoleCommandExecutionStrategy());
    }
}
