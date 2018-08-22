package com.iflytek.sas.voice.transfer.server;

import com.iflytek.sas.voice.transfer.server.client.FileTransportClient;
import com.iflytek.sas.voice.transfer.server.rpc.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jpli3
 */
@Slf4j
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    @Autowired
    private FileTransportClient fileTransportClient;

    @Autowired(required = false)
    private DemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }


    @Override
    public void run(String... args) {

    }
}
