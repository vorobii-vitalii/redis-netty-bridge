package io.vitaliivorobii.redis.netty.bridge.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.vitaliivorobii.redis.netty.bridge.domain.ClientRequest;
import io.vitaliivorobii.resp.types.RespArray;
import io.vitaliivorobii.resp.types.RespBulkString;
import io.vitaliivorobii.resp.types.RespDataType;

import java.util.List;

public class ClientRequestDecoder extends MessageToMessageDecoder<RespDataType> {

    @Override
    protected void decode(ChannelHandlerContext ctx, RespDataType msg, List<Object> out) {
        if (msg instanceof RespArray(List<RespDataType> elements)) {
            boolean allBulkStrings = elements.stream().allMatch(v -> v instanceof RespBulkString);
            if (!allBulkStrings) {
                throw new DecoderException("Expected array of bulk strings!");
            }
            if (elements.isEmpty()) {
                throw new DecoderException("Expected at least command name");
            }
            String commandName = ((RespBulkString) elements.getFirst()).data().toUpperCase();

            List<String> commandArguments = elements.stream()
                    .skip(1)
                    .map(v -> ((RespBulkString) v))
                    .map(RespBulkString::data)
                    .toList();

            out.add(new ClientRequest(commandName, commandArguments));
        } else {
            throw new DecoderException("Expected array of bulk strings!");
        }
    }

}
