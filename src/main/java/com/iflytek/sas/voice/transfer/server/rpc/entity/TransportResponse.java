package com.iflytek.sas.voice.transfer.server.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: JiangPing Li
 * @date: 2018-08-07 11:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportResponse implements Serializable {
    private static final long serialVersionUID = 786160175234046271L;

    private Integer code;
    private String id;
    private String name;
    private String message;
}
