package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.RedisCommand;
import io.vitaliivorobii.redis.netty.bridge.command.args.LRangeArgs;
import io.vitaliivorobii.redis.netty.bridge.command.impl.RegexBasedCreateCommandStrategy;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.DatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.regex.Pattern;

public class RedisInsightProxyLRangeCommandStrategy extends RegexBasedCreateCommandStrategy<LRangeArgs> {
    private final DatabaseKey databaseKey;
    private final DatabaseIdProvider databaseIdProvider;
    private final URI redisInsightApiBaseUri;
    private final HttpClient httpClient;

    public RedisInsightProxyLRangeCommandStrategy(
            Pattern keyPattern,
            DatabaseKey databaseKey,
            DatabaseIdProvider databaseIdProvider,
            URI redisInsightApiBaseUri,
            HttpClient httpClient
    ) {
        super(LRangeArgs::key, keyPattern);
        this.databaseKey = databaseKey;
        this.databaseIdProvider = databaseIdProvider;
        this.redisInsightApiBaseUri = redisInsightApiBaseUri;
        this.httpClient = httpClient;
    }

    @Override
    public RedisCommand createCommand(ChannelHandlerContext context, String[] parsedArgs, LRangeArgs args) {
        return new DownloadListElementsCommand(
                redisInsightApiBaseUri,
                httpClient,
                databaseIdProvider.getDatabaseId(databaseKey),
                args,
                context
        );
    }

}
