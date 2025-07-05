package io.vitaliivorobii.redis.netty.bridge.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vitaliivorobii.redis.netty.bridge.command.get.RegexGetDataStrategy;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.regex.Pattern;

public class GetRatesDataStrategy extends RegexGetDataStrategy {

    private static final Logger log = LoggerFactory.getLogger(GetRatesDataStrategy.class);
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();

    private final Gson gson = new Gson();

    public GetRatesDataStrategy() {
        super(Pattern.compile("^RATES_([A-Z]{3})_([A-Z]{3})$"));
    }

    @Override
    protected CompletableFuture<RespDataType> fetch(String key, String[] parsedArgs) {
        String fromCurrency = parsedArgs[0];
        String toCurrency = parsedArgs[1];

        HttpRequest fetchRatesRequest = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .uri(URI.create("https://api.exchangerate-api.com/v4/latest/" + fromCurrency))
                .build();

        return httpClient.sendAsync(fetchRatesRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply((Function<HttpResponse<String>, RespDataType>) response -> {
                    log.info("Http response from rates server: {}", response);
                    if (response.statusCode() == HttpResponseStatus.OK.code()) {
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        return new RespBulkString(
                                jsonObject.getAsJsonObject("rates").get(toCurrency).getAsString()
                        );
                    } else {
                        log.warn("Error on fetch of rates {}", response.body());
                        return new RespNull();
                    }
                })
                .exceptionally(err -> {
                    log.info("Error on fetch of rates", err);
                    return new RespNull();
                });
    }
}
