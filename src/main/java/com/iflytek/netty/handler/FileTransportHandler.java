package com.iflytek.netty.handler;

import com.iflytek.netty.rpc.entity.TransportRequest;
import com.iflytek.netty.rpc.entity.TransportResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JiangPing Li
 * @date: 2018-08-07 11:47
 */
@Slf4j
public class FileTransportHandler extends ChannelHandlerAdapter {

    private TransportRequest transportRequest;

    public FileTransportHandler(TransportRequest transportRequest) {
        this.transportRequest = transportRequest;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        /**
         * 会在连接被建立并且准备进行通信时被调用
         * Unpooled.copiedBuffer(transportRequest.getAttachment())
         */
        final ChannelFuture f = ctx.channel().writeAndFlush(transportRequest);
        // 消息发送完毕后 关闭连接 不会立即关闭
//        f.addListener((ChannelFutureListener) future -> {
//            assert f == future;
//            ctx.close();
//        });
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));

    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            TransportResponse transportResponse = (TransportResponse) msg;
            log.info("---收到传输响应---" + transportResponse.toString());
        } finally {
            ReferenceCountUtil.release(msg);
//            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
