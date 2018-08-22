package com.iflytek.sas.voice.transfer.server.common;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 记录调用方法的元信息
 * @author: JiangPing Li
 * @date: 2018-08-06 9:44
 */
@Data
@Component
public class MethodInvokeMeta implements Serializable {
    private static final long serialVersionUID = 8524001951499585813L;
    private Class<?> interfaceClass;
    private String methodName;
    private Object[] args;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;
}
