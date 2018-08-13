package com.iflytek.netty.rpc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: JiangPing Li
 * @date: 2018-08-07 11:32
 */
@Data
public class TransportResponse implements Serializable {
    private static final long serialVersionUID = 786160175234046271L;

    private String id;
    private String name;
    private String message;
}
