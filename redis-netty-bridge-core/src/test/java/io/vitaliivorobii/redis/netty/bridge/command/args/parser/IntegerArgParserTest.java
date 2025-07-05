package io.vitaliivorobii.redis.netty.bridge.command.args.parser;

import io.vavr.control.Either;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerArgParserTest {

    IntegerArgParser integerArgParser = new IntegerArgParser();

    @Test
    void givenEmptyArgsList() {
        ClientRequest clientRequest = new ClientRequest("COMMAND", List.of());
        Either<Integer, String> result = integerArgParser.parse(clientRequest);
        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo("Expected exactly one argument");
    }

    @Test
    void givenListOfArgsContainsMoreOneValue() {
        ClientRequest clientRequest = new ClientRequest("COMMAND", List.of("1", "2", "3"));
        Either<Integer, String> result = integerArgParser.parse(clientRequest);
        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo("Expected exactly one argument");
    }

    @Test
    void givenWronlyFormatterArgument() {
        ClientRequest clientRequest = new ClientRequest("COMMAND", List.of("9g2"));
        Either<Integer, String> result = integerArgParser.parse(clientRequest);
        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo("Parameter is not an integer");
    }

    @Test
    void givenCorrectlyFormattedArgument() {
        ClientRequest clientRequest = new ClientRequest("COMMAND", List.of("9123"));
        Either<Integer, String> result = integerArgParser.parse(clientRequest);
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(9123);
    }
}
