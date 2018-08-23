package com.iflytek.sas.voice.transfer.server.client;

import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
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

    public int getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public NettyClient(String url, int port) {
        this.url = url;
        this.port = port;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    }

    @PreDestroy
    public void close() {
        log.info("关闭资源");
        worker.shutdownGracefully();
    }

    public Object remoteCall(final MethodInvokeMeta cmd) {
        try {
            ClientChannelInitializer customChannelInitializer = new ClientChannelInitializer(cmd);
            bootstrap.handler(customChannelInitializer);
            ChannelFuture future = bootstrap.connect(url, port).sync();
            future.channel().closeFuture().sync();
            return customChannelInitializer.getResponse();
        } catch (InterruptedException e) {
            log.error("===调用失败：",e);
            return null;
        }finally {
            //优雅的退出，释放NIO线程组
            worker.shutdownGracefully();
        }
    }
}
