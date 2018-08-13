package com.iflytek.netty.common;

import java.io.Serializable;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 9:50
 */
public class NullWritable implements Serializable {
    private static final long serialVersionUID = -7589666158575232386L;
    private static NullWritable instance = new NullWritable();

    private NullWritable() {
    }

    public static NullWritable nullWritable() {
        return instance;
    }
}
