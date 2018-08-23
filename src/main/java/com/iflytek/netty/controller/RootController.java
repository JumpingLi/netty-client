package com.iflytek.netty.controller;

import com.iflytek.netty.rpc.entity.TransportRequest;
import com.iflytek.netty.rpc.entity.TransportResponse;
import com.iflytek.netty.rpc.service.DemoService;
import com.iflytek.netty.rpc.util.GzipUtils;
import com.iflytek.netty.rpc.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 15:14
 */
@Slf4j
@RestController
public class RootController {

    @Resource
    private DemoService demoService;

    @RequestMapping(path = {"/test/{a}/{b}"}, method = RequestMethod.GET)
    public Object remoteCall(@PathVariable Integer a,
                             @PathVariable Integer b) {
        return demoService.sum(a,b);
    }

    @RequestMapping(path = {"/transport/{n}"}, method = RequestMethod.GET)
    public Object testFileTransfer(@PathVariable Integer n) {
        ExecutorService pool = ThreadPoolUtils.getExecutorService("netty-client-pool-%d");
        Long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String path = "F:\\file_source\\4s.wav";
            pool.execute(new FileTransportRunnable(path));
        }
        Long time = System.currentTimeMillis() - start;
        log.debug("===耗时=== " + time);
        return "ok";
    }

    private class FileTransportRunnable implements Runnable {
        private String path;

        private FileTransportRunnable(String path) {
            super();
            this.path = path;
        }

        @Override
        public void run() {
            try {
                File file = new File(path);
                TransportRequest req = new TransportRequest();
                req.setId(UUID.randomUUID().toString().replaceAll("-",""));
                req.setAppend(Boolean.TRUE);
                req.setName(file.getName());
                FileInputStream in = new FileInputStream(file);
                int fileLength = in.available();
                byte[] allData = new byte[in.available()];
                if (in.read(allData) == -1) {
                    in.close();
                }
//                分片段 传输
                int part = 8192;
                int start = 0;
                int mod = fileLength / part;
                int rem = fileLength % part;
                for (int i = 1; i <= mod; i++) {
                    req.setMessage("分段批次: " + i);
                    byte[] data = new byte[part];
                    System.arraycopy(allData, start, data, 0, part);
                    start = start + part;
                    // 进行数据的压缩
                    req.setAttachment(GzipUtils.gzip(data));
                    demoService.fileTransport(req);
//                    log.debug("---client Received: " + req.getId() + ", " + req.getName() + ", " + req.getMessage());
                }
                if (rem > 0) {
                    req.setMessage("分段批次: " + (mod+1));
                    byte[] data = new byte[rem];
                    System.arraycopy(allData, start, data, 0, rem);
                    if (mod < 1) {
                        req.setAppend(Boolean.FALSE);
                    }
                    req.setAttachment(GzipUtils.gzip(data));
                    demoService.fileTransport(req);
//                    log.debug("---client Received: " + req.getId() + ", " + req.getName() + ", " + req.getMessage());
                }
            } catch (Exception e) {
                log.error("---传输文件异常: ", e);
            }
        }
    }

    @RequestMapping(path = {"/test/{fileName}"}, method = RequestMethod.GET)
    public Mono<TransportResponse> testFileTransport(@PathVariable String fileName) throws Exception {

        // 进行文件的读取
        String path = System.getProperty("user.dir") + File.separatorChar + "send"
                + File.separatorChar + fileName;
        File file = new File(path);
        TransportRequest req = new TransportRequest();
        req.setName(fileName);
        req.setMessage("分段批次: " + 1);
        req.setAppend(Boolean.TRUE);
        req.setId(UUID.randomUUID().toString().replaceAll("-",""));
        FileInputStream in = new FileInputStream(file);
        int fileLength = in.available();
        log.debug("===========文件字节数: " + fileLength);
        byte[] allData = new byte[fileLength];
        if (in.read(allData) == -1) {
            in.close();
        }
        // 分片段 传输
//        int part = 1024;
//        int start = 0;
//        int mod = fileLength / part;
//        int rem = fileLength % part;
//        for (int i = 0; i < mod; i++) {
//            byte[] data = new byte[part];
//            System.arraycopy(allData,start,data,0,part);
//            start = start + part;
//            // 进行数据的压缩
//            req.setAttachment(GzipUtils.gzip(data));
//            req.setId(data.length + "");
//            demoService.fileTransport(req);
//        }
//        if (rem > 0){
//            byte[] data = new byte[rem];
//            System.arraycopy(allData,start,data,0,rem);
//            if (mod < 1){
//                req.setAppend(Boolean.FALSE);
//            }
//            req.setAttachment(GzipUtils.gzip(data));
//            req.setId(data.length + "");
//            demoService.fileTransport(req);
//        }
        req.setAttachment(GzipUtils.gzip(allData));
        return Mono.create(cityMonoSink -> cityMonoSink.success(demoService.fileTransport(req)));

//        ChannelFuture f = fileTransferClient.getSocketChannel().writeAndFlush(req);
//        f.addListener((ChannelFutureListener) future -> {
//            assert f == future;
//            log.info("---传输成功---" + future.isSuccess());
//        });
//        log.info("---发送完毕---");
//        return "ok";

//        return fileTransferClient.connect();
//        FileTransportClient client = new FileTransportClient("127.0.0.1",12345);
//        return client.connect(req);
    }
}
