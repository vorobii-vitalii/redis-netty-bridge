package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NoArgsExpectedParserTest {

    NoArgsExpectedParser parser = new NoArgsExpectedParser();

    @Test
    void givenArgumentsPassed() {
        ClientRequest request = new ClientRequest("COMMAND", List.of("x", "y"));
        Either<Void, String> parseResult = parser.parse(request);
        assertThat(parseResult.isRight()).isTrue();
        assertThat(parseResult.get()).isEqualTo("No arguments expected");
    }

    @Test
    void givenArgumentsNotPassed() {
        ClientRequest request = new ClientRequest("COMMAND", List.of());
        Either<Void, String> parseResult = parser.parse(request);
        assertThat(parseResult.isLeft()).isTrue();
    }
}