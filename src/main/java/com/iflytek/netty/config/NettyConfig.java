package com.iflytek.netty.config;

import com.iflytek.netty.client.FileTransportClient;
import com.iflytek.netty.client.NettyClient;
import com.iflytek.netty.rpc.util.NettyBeanScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jpli3
 */
@Configuration
public class NettyConfig {

    @Bean
    public NettyBeanScanner initNettyBeanScanner(@Value("${netty.basePackage}") String basePackage,
                                                 @Value("${netty.clientName}") String clientName) {
        return new NettyBeanScanner(basePackage, clientName);
    }

    @Bean("nettyClient")
    public NettyClient initNettyClient(@Value("${netty.url}") String url,
                                       @Value("${netty.port}") int port) {
        return new NettyClient(url, port);
    }
//
//    @Bean("fileTransportClient")
//    public FileTransportClient initFileTransferClient(@Value("${netty.url}") String url,
//                                                      @Value("${netty.port}") int port) {
//        return new FileTransportClient(url, port);
//    }

}
