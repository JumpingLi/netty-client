package com.iflytek.netty;

import com.iflytek.netty.rpc.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author jpli3
 */
@Slf4j
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    @Resource
    private DemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
//        String path = "F:\\file_source\\0.jpg";
//        File file = new File(path);
//        TransportRequest req = new TransportRequest();
//        req.setId(file.length() + "");
//        req.setName(file.getName());
//        req.setMessage("文件信息-" + file.getName());
//        FileInputStream in = new FileInputStream(file);
//        byte[] data = new byte[in.available()];
//        if (in.read(data) == -1) {
//            in.close();
//        }
//        req.setAttachment(GzipUtils.gzip(data));
//            FileTransportClient client = new FileTransportClient("127.0.0.1",12345);
//        fileTransportClient.connect(req);
    }
}
