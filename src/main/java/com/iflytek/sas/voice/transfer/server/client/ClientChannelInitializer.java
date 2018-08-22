package com.iflytek.sas.voice.transfer.server.client;

import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;
import com.iflytek.sas.voice.transfer.server.common.NullWritable;
import com.iflytek.sas.voice.transfer.server.handler.ClientChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 11:26
 */
@Slf4j
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private MethodInvokeMeta methodInvokeMeta;

    private Object response;

    public ClientChannelInitializer(MethodInvokeMeta methodInvokeMeta) {
//        if (!"toString".equals(methodInvokeMeta.getMethodName())) {
//            log.info("[ClientChannelInitializer] 调用方法名：{}, 参数类型：{}, 返回值类型{}"
//                    , methodInvokeMeta.getMethodName()
////                    , methodInvokeMeta.getArgs()
//                    , methodInvokeMeta.getParameterTypes()
//                    , methodInvokeMeta.getReturnType());
//        }
        this.methodInvokeMeta = methodInvokeMeta;
    }

    public Object getResponse() {
        if (response instanceof NullWritable) {
            return null;
        }
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    protected void initChannel(SocketChannel  ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                ClassResolvers.weakCachingConcurrentResolver(null)));
        pipeline.addLast(new ClientChannelHandler(methodInvokeMeta, this));
    }
}
