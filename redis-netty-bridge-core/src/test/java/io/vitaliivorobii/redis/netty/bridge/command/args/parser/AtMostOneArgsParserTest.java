package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AtMostOneArgsParserTest {

    AtMostOneArgsParser parser = new AtMostOneArgsParser();

    @Test
    void givenMoreThanOneArgProvided() {
        ClientRequest request = new ClientRequest("COMMAND", List.of("a", "b"));
        Either<Optional<String>, String> parseResult = parser.parse(request);
        assertThat(parseResult.isRight()).isTrue();
        assertThat(parseResult.get()).isEqualTo("Expected at most one param, but was given 2 params");
    }

    @Test
    void givenExactlyOneArgProvided() {
        ClientRequest request = new ClientRequest("COMMAND", List.of("a"));
        Either<Optional<String>, String> parseResult = parser.parse(request);
        assertThat(parseResult.isLeft()).isTrue();
        assertThat(parseResult.getLeft()).contains("a");
    }

    @Test
    void givenZeroArgsProvided() {
        ClientRequest request = new ClientRequest("COMMAND", List.of());
        Either<Optional<String>, String> parseResult = parser.parse(request);
        assertThat(parseResult.isLeft()).isTrue();
        assertThat(parseResult.getLeft()).isEmpty();
    }
}
