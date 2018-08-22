package com.iflytek.sas.voice.transfer.server.rpc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: JiangPing Li
 * @date: 2018-08-07 11:30
 */
@Data
public class TransportRequest implements Serializable {
    private static final long serialVersionUID = 7421653069871857094L;

    private String id;
    private String name;
    private String message;
    private byte[] attachment;
    private Boolean append;
}
