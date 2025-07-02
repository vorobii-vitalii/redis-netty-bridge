package io.vitaliivorobii.redis.netty.bridge;

import io.vitaliivorobii.redis.netty.bridge.command.impl.CommandNameDelegatingCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.command.impl.GenericCommandFactory;
import io.vitaliivorobii.redis.netty.bridge.command.impl.NullCommand;
import io.vitaliivorobii.redis.netty.bridge.command.parser.GetCommandArgsParser;
import io.vitaliivorobii.redis.netty.bridge.command.parser.LRangeArgsParser;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.command.RedisInsightProxyGetCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.command.RedisInsightProxyLRangeCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.impl.HttpDatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.server.RedisNettyBridge;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .version(HttpClient.Version.HTTP_2)
                .build();
        URI redisInsightBaseURI = URI.create("http://localhost:5540");
        RedisNettyBridge redisNettyBridge = new RedisNettyBridge(
                7000,
                new CommandNameDelegatingCommandFactory(Map.of(
                        "GET", new GenericCommandFactory<>(new GetCommandArgsParser(), List.of(
                                new RedisInsightProxyGetCommandStrategy(
                                        Pattern.compile("^(.*)$"),
                                        new DatabaseKey(
                                                "redis-12333.c232.us-east-1-2.ec2.redns.redis-cloud.com:12333",
                                                null
                                        ),
                                        new HttpDatabaseIdProvider(httpClient, redisInsightBaseURI),
                                        redisInsightBaseURI,
                                        httpClient
                                )
                        ), NullCommand::new),
                        "LRANGE", new GenericCommandFactory<>(new LRangeArgsParser(), List.of(
                                new RedisInsightProxyLRangeCommandStrategy(
                                        Pattern.compile("^(.*)$"),
                                        new DatabaseKey(
                                                "redis-12333.c232.us-east-1-2.ec2.redns.redis-cloud.com:12333",
                                                null
                                        ),
                                        new HttpDatabaseIdProvider(httpClient, redisInsightBaseURI),
                                        redisInsightBaseURI,
                                        httpClient
                                )
                        ), NullCommand::new)
                ))
        );
        redisNettyBridge.start().sync();
    }
}