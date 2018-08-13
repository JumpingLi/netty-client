package com.iflytek.netty.controller;

import com.iflytek.netty.client.FileTransportClient;
import com.iflytek.netty.rpc.entity.FileTransferDO;
import com.iflytek.netty.rpc.entity.TransportRequest;
import com.iflytek.netty.rpc.service.DemoService;
import com.iflytek.netty.rpc.util.GzipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 15:14
 */
@Slf4j
@RestController
public class RootController {

    @Resource
    private DemoService demoService;

    @Autowired
    private FileTransportClient fileTransferClient;

    @RequestMapping(path = {"/test/{a}/{b}"},method = RequestMethod.GET)
    public Object remoteCall(@PathVariable Integer a,
                             @PathVariable Integer b){
        return demoService.print();
    }

    @RequestMapping(path = {"/transport/{fileName}"}, method = RequestMethod.GET)
    public Object testFileTransfer(@PathVariable String fileName) {
        FileTransferDO uploadFile = new FileTransferDO();
        uploadFile.setPath("F:\\file_source\\"+fileName);
        uploadFile.setFileName(fileName);
        uploadFile.setStarPos(0);
        return "ok";
    }

    @RequestMapping(path = {"/test/{fileName}"}, method = RequestMethod.GET)
    public Object testFileTransport(@PathVariable String fileName) throws Exception {

        // 进行文件的读取
        String path = System.getProperty("user.dir") + File.separatorChar + "send"
                + File.separatorChar + fileName;
        File file = new File(path);
        TransportRequest req = new TransportRequest();
//        req.setId(file.length() + "");
        req.setName(file.getName());
        req.setMessage("文件信息-" + fileName);
        req.setAppend(Boolean.TRUE);
        // 进行图片的读取
        FileInputStream in = new FileInputStream(file);
        int fileLength = in.available();
        log.debug("===========文件字节数: " + fileLength);
        byte[] allData = new byte[fileLength];
        if (in.read(allData) == -1) {
            in.close();
        }
        // 分片段 传输
        int part = 1024;
        int start = 0;
        int mod = fileLength / part;
        int rem = fileLength % part;
        for (int i = 0; i < mod; i++) {
            byte[] data = new byte[part];
            System.arraycopy(allData,start,data,0,part);
            start = start + part;
            // 进行数据的压缩
            req.setAttachment(GzipUtils.gzip(data));
            req.setId(data.length + "");
            demoService.fileTransport(req);
        }
        if (rem > 0){
            byte[] data = new byte[rem];
            System.arraycopy(allData,start,data,0,rem);
            if (mod < 1){
                req.setAppend(Boolean.FALSE);
            }
            req.setAttachment(GzipUtils.gzip(data));
            req.setId(data.length + "");
            demoService.fileTransport(req);
        }
        return "OK";

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
