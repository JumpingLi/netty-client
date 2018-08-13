package com.iflytek.netty;

import com.iflytek.netty.client.FileTransportClient;
import com.iflytek.netty.rpc.entity.TransportRequest;
import com.iflytek.netty.rpc.util.GzipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author jpli3
 */
@Slf4j
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    @Autowired
    private FileTransportClient fileTransportClient;

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        String path = "F:\\file_source\\0.jpg";
            File file = new File(path);
            TransportRequest req = new TransportRequest();
            req.setId(file.length() + "");
            req.setName(file.getName());
            req.setMessage("文件信息-" + file.getName());
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[in.available()];
            if (in.read(data) == -1){
                in.close();
            }
            req.setAttachment(GzipUtils.gzip(data));
//            FileTransportClient client = new FileTransportClient("127.0.0.1",12345);
//        fileTransportClient.connect(req);

//        ExecutorService pool = ThreadPoolUtils.getExecutorService("netty-client-pool-%d");
//        for (int i = 2; i < 53; i++) {
//            String path = "F:\\file_source\\" + "1 - 副本 (" + i + ").txt";
//            pool.execute(new FileTransportRunnable(path));
//
//        }
    }

    private class FileTransportRunnable implements Runnable{
        private String path;

        private FileTransportRunnable(String path){
            super();
            this.path = path;
        }
        @Override
        public void run() {
            try{
                File file = new File(path);
                TransportRequest req = new TransportRequest();
                req.setId(file.length() + "");
                req.setName(file.getName());
                req.setMessage("文件信息-" + file.getName());
                FileInputStream in = new FileInputStream(file);
                byte[] data = new byte[in.available()];
                if (in.read(data) == -1){
                    in.close();
                }
                req.setAttachment(GzipUtils.gzip(data));
                FileTransportClient client = new FileTransportClient("192.168.57.84",12345);
                client.connect(req);
            }catch (Exception e){
                log.error("---传输文件异常: ",e);
            }
        }
    }
}
