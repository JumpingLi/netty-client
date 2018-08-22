package com.iflytek.sas.voice.transfer.server.client;

import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 11:20
 */
@Slf4j
public class NettyClient {
    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private int port;
    private String url;
    private int MAX_RETRY_TIMES = 10;

    public NettyClient(String url, int port) {
        this.url = url;
        this.port = port;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }

    @PreDestroy
    public void close() {
        log.info("关闭资源");
        worker.shutdownGracefully();
    }

    public Object remoteCall(final MethodInvokeMeta cmd, int retry) {
        try {
            ClientChannelInitializer customChannelInitializer = new ClientChannelInitializer(cmd);
            bootstrap.handler(customChannelInitializer);
            ChannelFuture sync = bootstrap.connect(url, port).sync();
            sync.channel().closeFuture().sync();
            Object response = customChannelInitializer.getResponse();
            return response;
        } catch (InterruptedException e) {
            retry++;
            if (retry > MAX_RETRY_TIMES) {
                throw new RuntimeException("调用Wrong");
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                log.info("第{}次尝试....失败", retry);
                return remoteCall(cmd, retry);
            }
        }
    }
}
