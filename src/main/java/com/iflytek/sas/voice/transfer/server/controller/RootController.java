package com.iflytek.sas.voice.transfer.server.controller;

import com.iflytek.sas.voice.transfer.server.client.FileTransportClient;
import com.iflytek.sas.voice.transfer.server.rpc.entity.TransportRequest;
import com.iflytek.sas.voice.transfer.server.rpc.entity.TransportResponse;
import com.iflytek.sas.voice.transfer.server.rpc.service.DemoService;
import com.iflytek.sas.voice.transfer.server.rpc.util.GzipUtils;
import com.iflytek.sas.voice.transfer.server.rpc.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 15:14
 */
@Slf4j
@RestController
public class RootController {

    @Autowired(required = false)
    private DemoService demoService;

    @RequestMapping(path = {"/test/{a}/{b}"}, method = RequestMethod.GET)
    public Object remoteCall(@PathVariable Integer a,
                             @PathVariable Integer b) {
        return demoService.print();
    }

    @RequestMapping(path = {"/transport"}, method = RequestMethod.GET)
    public Object testFileTransfer() throws Exception {
        ExecutorService pool = ThreadPoolUtils.getExecutorService("netty-client-pool-%d");
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            String path = "F:\\file_source\\4s.wav";
            pool.execute(new FileTransportRunnable(path));
        }
        Long time = System.currentTimeMillis() - startTime;
        log.debug("===耗时=== " + time);
//        String path = "F:\\file_source\\4s.wav";
//        File file = new File(path);
//        TransportRequest req = new TransportRequest();
//        req.setId(UUID.randomUUID().toString().replace("-", ""));
//        req.setName(file.getName());
//        req.setAppend(Boolean.TRUE);
//        FileInputStream in = new FileInputStream(file);
//        byte[] allData = new byte[in.available()];
//        if (in.read(allData) == -1) {
//            in.close();
//        }
//        int fileLength = allData.length;
//        int part = 8192;
//        int start = 0;
//        int mod = fileLength / part;
//        int rem = fileLength % part;
//        for (int i = 0; i < mod; i++) {
//            byte[] data = new byte[part];
//            System.arraycopy(allData, start, data, 0, part);
//            start = start + part;
//            // 进行数据的压缩
//            req.setAttachment(GzipUtils.gzip(data));
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(req.getAttachment());
//            String md5Str = bytesToHex1(md5.digest());
//            req.setMessage(md5Str);
//            demoService.fileTransport(req);
//        }
//        if (rem > 0) {
//            byte[] data = new byte[rem];
//            System.arraycopy(allData, start, data, 0, rem);
//            req.setAttachment(GzipUtils.gzip(data));
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(req.getAttachment());
//            String md5Str = bytesToHex1(md5.digest());
//            req.setMessage(md5Str);
//            demoService.fileTransport(req);
//        }
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
                req.setId(UUID.randomUUID().toString().replace("-", ""));
                req.setName(file.getName());
                req.setAppend(Boolean.TRUE);
                FileInputStream in = new FileInputStream(file);
                byte[] allData = new byte[in.available()];
                if (in.read(allData) == -1) {
                    in.close();
                }
                int fileLength = allData.length;
                int part = 8192;
                int start = 0;
                int mod = fileLength / part;
                int rem = fileLength % part;
                for (int i = 0; i < mod; i++) {
                    byte[] data = new byte[part];
                    System.arraycopy(allData, start, data, 0, part);
                    start = start + part;
                    // 进行数据的压缩
                    req.setAttachment(GzipUtils.gzip(data));
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(req.getAttachment());
                    String md5Str = bytesToHex1(md5.digest());
                    req.setMessage(md5Str);
                    demoService.fileTransport(req);
                    log.debug("---client send: 文件Id=" + req.getId() + ", 文件名=" + req.getName() + ", 分段md5=" + req.getMessage());
                }
                if (rem > 0) {
                    byte[] data = new byte[rem];
                    System.arraycopy(allData, start, data, 0, rem);
                    req.setAttachment(GzipUtils.gzip(data));
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(req.getAttachment());
                    String md5Str = bytesToHex1(md5.digest());
                    req.setMessage(md5Str);
                    demoService.fileTransport(req);
                    log.debug("---client send: 文件Id=" + req.getId() + ", 文件名=" + req.getName() + ", 分段md5=" + req.getMessage());
                }
            } catch (Exception e) {
                log.error("---传输文件异常: ", e);
            }
        }
    }

    @RequestMapping(path = {"/test/{fileName}"}, method = RequestMethod.GET)
    public TransportResponse testFileTransport(@PathVariable String fileName) throws Exception {

        // 进行文件的读取
        String path = System.getProperty("user.dir") + File.separatorChar + "send"
                + File.separatorChar + fileName + ".wav";
        File file = new File(path);
        TransportRequest req = new TransportRequest();
//        String[] nameArray = fileName.split("\\.");
//        req.setName(nameArray[0] + "_" + UUID.randomUUID() + "." + nameArray[1]);
        req.setName(fileName + ".wav");
        req.setMessage("文件信息-" + fileName);
        FileInputStream in = new FileInputStream(file);
//        int fileLength = in.available();
//        log.debug("===========文件字节数: " + fileLength);
//        byte[] allData = new byte[fileLength];
//        if (in.read(allData) == -1) {
//            in.close();
//        }
        req.setId(UUID.randomUUID().toString().replace("-", ""));
        // 分片段 传输

        TransportResponse response;
        // 音频流报文发送
        byte[] buf = new byte[8192];
        int len;
        int i = 1;
        while ((len = in.read(buf)) != -1) {
            if (i == 1) {
                req.setAppend(false);
            } else if (len != 8192) {
                req.setAppend(true);
            } else {
                req.setAppend(false);
            }
            i++;
            req.setAttachment(GzipUtils.gzip(buf));
            Thread.sleep(len / 16);
            Instant start = Instant.now();

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(req.getAttachment());
            String md5Str = bytesToHex1(md5.digest());
            log.debug("调用服务端：{},{},{}", req.getId(), req.getAppend(), md5Str);
            response = demoService.audioTransport(req);
            log.debug("i = " + i + "code = " + response.getCode());
            if ("1000".equals(response.getCode() + "")) {
                Instant end = Instant.now();
                log.error("id:" + req.getId() + ",filename: " + fileName + ", response : " + response.toString());
                log.error("id:" + req.getId() + ", statistic-time:" + Duration.between(start, end).toMillis());
            }
        }
        return TransportResponse.builder().message("ok").build();
//        req.setId(allData.length + "");
//        req.setAttachment(GzipUtils.gzip(allData));
//        return demoService.audioTransport(req);

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

    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int temp = 0xff & md5Array[i];
            String hexString = Integer.toHexString(temp);
            if (hexString.length() == 1) {
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
    }
}
