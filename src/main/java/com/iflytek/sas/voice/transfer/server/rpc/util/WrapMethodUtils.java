package com.iflytek.sas.voice.transfer.server.rpc.util;

import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;

import java.lang.reflect.Method;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 11:49
 */
public class WrapMethodUtils {
    public static MethodInvokeMeta readMethod(Class interfaceClass, Method method, Object[] args) {
        MethodInvokeMeta mim = new MethodInvokeMeta();
        mim.setInterfaceClass(interfaceClass);
        mim.setArgs(args);
        mim.setMethodName(method.getName());
        mim.setReturnType(method.getReturnType());
        Class<?>[] parameterTypes = method.getParameterTypes();
        mim.setParameterTypes(parameterTypes);
        return mim;
    }
}
