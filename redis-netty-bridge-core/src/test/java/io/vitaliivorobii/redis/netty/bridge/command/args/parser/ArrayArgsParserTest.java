package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArrayArgsParserTest {

    ArrayArgsParser arrayArgsParser = new ArrayArgsParser();

    @Test
    void shouldJustCopyPassedParams() {
        ClientRequest clientRequest = new ClientRequest("COMMAND", List.of("a", "b", "c"));
        Either<List<String>, String> parseResult = arrayArgsParser.parse(clientRequest);
        assertThat(parseResult.isLeft()).isTrue();
        assertThat(parseResult.getLeft()).containsExactly("a", "b", "c");
    }
}