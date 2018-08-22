package com.iflytek.sas.voice.transfer.server.handler;

import com.iflytek.sas.voice.transfer.server.client.ClientChannelInitializer;
import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 11:32
 */
@Slf4j
public class ClientChannelHandler extends SimpleChannelInboundHandler {
    private MethodInvokeMeta methodInvokeMeta;
    private ClientChannelInitializer clientChannelInitializer;

    public ClientChannelHandler(MethodInvokeMeta methodInvokeMeta,
                                ClientChannelInitializer clientChannelInitializer) {
        this.methodInvokeMeta = methodInvokeMeta;
        this.clientChannelInitializer = clientChannelInitializer;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端出异常了,异常信息:{}", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        if (!methodInvokeMeta.getMethodName().endsWith("toString")
//                && !"class java.lang.String".equals(methodInvokeMeta.getReturnType().toString())){
//            log.info("客户端调用方法名:{},信息返回值类型：{}",
////                    methodInvokeMeta.getArgs(),
//                    methodInvokeMeta.getMethodName(),
//                    methodInvokeMeta.getReturnType());
//        }
        ctx.writeAndFlush(methodInvokeMeta);

    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
////        log.debug("---返回结果：" + msg.toString());
//        clientChannelInitializer.setResponse(msg);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        clientChannelInitializer.setResponse(msg);
    }
}
