package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.command.args.types.LRangeArgs;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LRangeArgsParserTest {

    LRangeArgsParser parser = new LRangeArgsParser();

    @Test
    void givenNotExactly3ArgsPassed() {
        ClientRequest request = new ClientRequest("LRANGE", List.of("1"));
        Either<LRangeArgs, String> parseResult = parser.parse(request);
        assertThat(parseResult.isRight()).isTrue();
        assertThat(parseResult.get()).isEqualTo("Expected 3 args - key, start, stop");
    }

    @Test
    void givenStartHasNonIntFormat() {
        ClientRequest request = new ClientRequest("LRANGE", List.of("key", "a", "2"));
        Either<LRangeArgs, String> parseResult = parser.parse(request);
        assertThat(parseResult.isRight()).isTrue();
        assertThat(parseResult.get()).isEqualTo("Invalid argument format for start");
    }

    @Test
    void givenEndHasNonIntFormat() {
        ClientRequest request = new ClientRequest("LRANGE", List.of("key", "1", "g"));
        Either<LRangeArgs, String> parseResult = parser.parse(request);
        assertThat(parseResult.isRight()).isTrue();
        assertThat(parseResult.get()).isEqualTo("Invalid argument format for end");
    }

    @Test
    void givenCorrectFormatForAllArgs() {
        ClientRequest request = new ClientRequest("LRANGE", List.of("key", "1", "4"));
        Either<LRangeArgs, String> parseResult = parser.parse(request);
        assertThat(parseResult.isLeft()).isTrue();
        assertThat(parseResult.getLeft()).isEqualTo(new LRangeArgs("key", 1, 4));
    }

}