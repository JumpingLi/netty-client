package com.iflytek.sas.voice.transfer.server.rpc.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 9:53
 */
public class ObjectCodec extends MessageToMessageCodec<ByteBuf, Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) {
        byte[] data = ObjectSerializerUtils.serializer(msg);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(data);
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        Object deSerializer = ObjectSerializerUtils.deSerializer(bytes);
        out.add(deSerializer);
    }
}
