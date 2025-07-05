package io.vitaliivorobii.redis.netty.bridge.decoder;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespInteger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientRequestDecoderTest {

    ClientRequestDecoder clientRequestDecoder = new ClientRequestDecoder();

    @Test
    void shouldReturnErrorIfNotGivenArrayOfBulkStrings() {
        EmbeddedChannel channel = new EmbeddedChannel(clientRequestDecoder);
        assertThrows(DecoderException.class,
                () -> channel.writeInbound(new RespArray(List.of(new RespInteger(24)))));
    }

    @Test
    void shouldConvertToClientRequestIfGivenArrayOfBulkStrings() {
        EmbeddedChannel channel = new EmbeddedChannel(clientRequestDecoder);
        channel.writeInbound(new RespArray(List.of(new RespBulkString("get"), new RespBulkString("X"))));
        assertThat(channel.<ClientRequest>readInbound())
                .isEqualTo(new ClientRequest("GET", List.of("X")));
    }
}