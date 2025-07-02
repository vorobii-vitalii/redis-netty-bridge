package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.impl.RegexBasedCreateCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.DatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.regex.Pattern;

public class RedisInsightProxyGetCommandStrategy extends RegexBasedCreateCommandStrategy<String> {
    private final DatabaseKey databaseKey;
    private final DatabaseIdProvider databaseIdProvider;
    private final URI redisInsightApiBaseUri;
    private final HttpClient httpClient;

    public RedisInsightProxyGetCommandStrategy(
            Pattern keyPattern,
            DatabaseKey databaseKey,
            DatabaseIdProvider databaseIdProvider,
            URI redisInsightApiBaseUri,
            HttpClient httpClient
    ) {
        super(v -> v, keyPattern);
        this.databaseKey = databaseKey;
        this.databaseIdProvider = databaseIdProvider;
        this.redisInsightApiBaseUri = redisInsightApiBaseUri;
        this.httpClient = httpClient;
    }

    @Override
    public RedisCommand createCommand(ChannelHandlerContext context, String[] args) {
        var key = args[0];
        return new DownloadStringValueCommand(
                redisInsightApiBaseUri,
                httpClient,
                databaseIdProvider.getDatabaseId(databaseKey),
                key,
                context
        );
    }

}
