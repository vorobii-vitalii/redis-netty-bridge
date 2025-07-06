package io.vitaliivorobii.redis.netty.bridge.command.get;

import io.vitaliivorobii.resp.types.RespDataType;
import io.vitaliivorobii.resp.types.RespInteger;
import io.vitaliivorobii.resp.types.RespNull;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class RegexGetDataStrategyTest {

    RegexGetDataStrategy regexGetDataStrategy = new TestRegexGetDataStrategy();

    @Test
    void givenNotMatchesPattern() {
        Optional<CompletableFuture<RespDataType>> actual = regexGetDataStrategy.getData("NOT_MATCH");
        assertThat(actual).isEmpty();
    }

    @Test
    void givenMatchesPattern() {
        Optional<CompletableFuture<RespDataType>> actual = regexGetDataStrategy.getData("TEST123");
        assertThat(actual).isPresent();
        assertThat(actual.get().getNow(new RespNull())).isEqualTo(new RespInteger(123));
    }

    private static class TestRegexGetDataStrategy extends RegexGetDataStrategy {
        public TestRegexGetDataStrategy() {
            super(Pattern.compile("^TEST(\\d+)$"));
        }

        @Override
        protected CompletableFuture<RespDataType> fetch(String key, String[] parsedArgs) {
            return CompletableFuture.completedFuture(new RespInteger(Integer.parseInt(parsedArgs[0])));
        }
    }
}