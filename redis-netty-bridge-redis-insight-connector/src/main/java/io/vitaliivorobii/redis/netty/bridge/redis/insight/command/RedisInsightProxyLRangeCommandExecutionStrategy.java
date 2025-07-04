package io.vitaliivorobii.redis.netty.bridge.redis.insight.command;

import io.netty.channel.ChannelHandlerContext;
import io.vitaliivorobii.redis.netty.bridge.command.args.types.LRangeArgs;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.DatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.regex.Pattern;

public class RedisInsightProxyLRangeCommandExecutionStrategy extends RegexBasedCommandExecutionStrategy<LRangeArgs> {
    private final DatabaseKey databaseKey;
    private final DatabaseIdProvider databaseIdProvider;
    private final URI redisInsightApiBaseUri;
    private final HttpClient httpClient;

    public RedisInsightProxyLRangeCommandExecutionStrategy(
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
