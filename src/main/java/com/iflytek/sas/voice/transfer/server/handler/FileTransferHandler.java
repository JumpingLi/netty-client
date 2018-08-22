package com.iflytek.sas.voice.transfer.server.handler;

import com.iflytek.sas.voice.transfer.server.rpc.entity.FileTransferDO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 16:15
 */
@Slf4j
public class FileTransferHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private FileTransferDO fileTransferDO;

    public FileTransferHandler(FileTransferDO fileTransferDO) {
        this.fileTransferDO = fileTransferDO;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        try {
            File file = new File(fileTransferDO.getPath());
            randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(fileTransferDO.getStarPos());
            // 总长 101
            lastLength = (int) randomAccessFile.length() / 10;
            // byte 数组长度 10
            byte[] bytes = new byte[lastLength];
            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                fileTransferDO.setEndPos(byteRead);
                fileTransferDO.setBytes(bytes);
                ctx.writeAndFlush(fileTransferDO);
            } else {
                log.info("文件已经读完");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof Integer) {
//            start = (Integer) msg;
//            if (start != -1) {
//                randomAccessFile = new RandomAccessFile(fileTransferDO.getFile(), "r");
//                randomAccessFile.seek(start);
//                log.info("块儿长度：" + (randomAccessFile.length() / 10));
//                log.info("长度：" + (randomAccessFile.length() - start));
//                int a = (int) (randomAccessFile.length() - start);
//                int b = (int) (randomAccessFile.length() / 10);
//                if (a < b) {
//                    lastLength = a;
//                }
//                byte[] bytes = new byte[lastLength];
//                log.info("-----------------------------" + bytes.length);
//                if ((byteRead = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start) > 0) {
//                    log.info("byte 长度：" + bytes.length);
//                    fileTransferDO.setEndPos(byteRead);
//                    fileTransferDO.setBytes(bytes);
//                    try {
//                        ctx.writeAndFlush(fileTransferDO);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    randomAccessFile.close();
//                    ctx.close();
//                    log.info("文件已经读完--------" + byteRead);
//                }
//            }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
