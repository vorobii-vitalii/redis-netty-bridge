package io.vitaliivorobii.redis.netty.bridge;

import io.vitaliivorobii.redis.netty.bridge.command.RedisCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.get.FirstMatchGetDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.get.GetDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.handshake.HelloCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.impl.CommandNameDelegatingCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.mget.MGETCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.ping.PingCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.role.RoleCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.select.SelectCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.command.sentinel.GetMasterAddressSentinelCommandExecutionStrategy;
import io.vitaliivorobii.redis.netty.bridge.command.sentinel.SentinelCommandExecutor;
import io.vitaliivorobii.redis.netty.bridge.data.GetRatesDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.data.RandomNumberGetDataStrategy;
import io.vitaliivorobii.redis.netty.bridge.server.RedisNettyBridge;

import java.util.List;
import java.util.Map;

public class Main {

    public static final int PORT = 7000;

    public static void main(String[] args) throws InterruptedException {
        GetDataStrategy getDataStrategy = new FirstMatchGetDataStrategy(
                List.of(
                        new GetRatesDataStrategy(),
                        new RandomNumberGetDataStrategy()
                )
        );
        RedisCommandExecutor redisCommandExecutor = new CommandNameDelegatingCommandExecutor(Map.of(
                "HELLO", new HelloCommandExecutor(),
                "SELECT", new SelectCommandExecutor(),
                "PING", new PingCommandExecutor(),
                "ROLE", new RoleCommandExecutor(),
                "GET", new GetCommandExecutor(getDataStrategy),
                "MGET", new MGETCommandExecutor(getDataStrategy),
                "SENTINEL", new SentinelCommandExecutor(List.of(
                        new GetMasterAddressSentinelCommandExecutionStrategy()
                ))
        ));
        RedisNettyBridge redisNettyBridge = new RedisNettyBridge(PORT, redisCommandExecutor);
        redisNettyBridge.start().await().sync();
    }
}
