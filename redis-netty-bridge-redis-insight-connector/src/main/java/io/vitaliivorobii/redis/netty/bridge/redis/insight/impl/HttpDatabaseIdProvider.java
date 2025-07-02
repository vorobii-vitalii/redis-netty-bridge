package io.vitaliivorobii.redis.netty.bridge.redis.insight.impl;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.DatabaseIdProvider;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseDTO;
import io.vitaliivorobii.redis.netty.bridge.redis.insight.dto.DatabaseKey;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class HttpDatabaseIdProvider implements DatabaseIdProvider {
    private static final int IGNORED_KEY = 1;
    private static final Duration DATABASES_CACHE_TTL = Duration.ofSeconds(30);

    private final HttpClient httpClient;
    private final URI redisInsightApiBaseUri;
    private final Gson gson = new Gson();
    private final AsyncLoadingCache<Integer, Map<DatabaseKey, String>> cache = Caffeine.newBuilder()
            .expireAfterWrite(DATABASES_CACHE_TTL)
            .buildAsync(new AsyncCacheLoader<>() {
                @Override
                public CompletableFuture<? extends Map<DatabaseKey, String>> asyncLoad(
                        Integer ignored,
                        Executor executor
                ) {
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .GET()
                            .version(HttpClient.Version.HTTP_1_1)
                            .uri(UriBuilder.fromUri(redisInsightApiBaseUri)
                                    .path("/api/databases")
                                    .build())
                            .build();
                    return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body)
                            .thenApply(body -> {
                                List<DatabaseDTO> databases = gson.fromJson(body, new TypeToken<>() {
                                });
                                return databases.stream()
                                        .collect(Collectors.toMap(
                                                v -> new DatabaseKey(v.name(), v.db()), DatabaseDTO::id));
                            });
                }
            });

    public HttpDatabaseIdProvider(HttpClient httpClient, URI redisInsightApiBaseUri) {
        this.httpClient = httpClient;
        this.redisInsightApiBaseUri = redisInsightApiBaseUri;
    }

    @Override
    public CompletionStage<String> getDatabaseId(DatabaseKey databaseKey) {
        return cache.get(IGNORED_KEY)
                .thenApply(v -> v.get(databaseKey));
    }
}
