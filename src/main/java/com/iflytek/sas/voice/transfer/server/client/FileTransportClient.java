package com.iflytek.sas.voice.transfer.server.client;

import com.iflytek.sas.voice.transfer.server.handler.FileTransportHandler;
import com.iflytek.sas.voice.transfer.server.rpc.entity.TransportRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 16:08
 */
@Slf4j
public class FileTransportClient {
    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private int port;
    private String url;
    private SocketChannel socketChannel;

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public FileTransportClient(String url, int port) {
        this.url = url;
        this.port = port;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();

    }

    public String connect(TransportRequest transportRequest) throws Exception {
        try {
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast(new FileTransportHandler(transportRequest));
                        }
                    });
            // 连接服务器
            ChannelFuture f = bootstrap.connect(url, port).sync();
            if (f.isSuccess()) {
                socketChannel = (SocketChannel) f.channel();
                log.debug("client连接server成功 host={},port={}", url, port);
            }
            // 等待链接关闭
            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully().sync();
        }
        return "Ok";
    }

}
