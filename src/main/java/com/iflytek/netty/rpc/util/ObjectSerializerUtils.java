package com.iflytek.netty.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 9:56
 */
@Slf4j
public class ObjectSerializerUtils {
    /**
     * 反序列化
     * @param data
     * @return
     */
    public static Object deSerializer(byte[] data) {
        if (data != null && data.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception e) {
                log.error("[反序列化异常:] {}", e);
            }
            return null;
        } else {
            log.info("[反序列化:] 入参为空");
            return null;
        }
    }

    /**
     * 序列化对象
     *
     * @param obj
     * @return
     */
    public static byte[] serializer(Object obj) {
        if (obj != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                oos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                log.error("[序列化异常:] {}", e);
            }
            return null;
        } else {
            log.info("[序列化:] 入参为空");
            return null;
        }
    }
}
