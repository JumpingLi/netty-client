package com.iflytek.sas.voice.transfer.server.client;

import com.iflytek.sas.voice.transfer.server.common.MethodInvokeMeta;
import com.iflytek.sas.voice.transfer.server.rpc.util.WrapMethodUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: JiangPing Li
 * @date: 2018-08-06 11:46
 */
@Slf4j
public class RpcProxyFactoryBean extends AbstractFactoryBean implements InvocationHandler {

    private Class interfaceClass;

    private NettyClient nettyClient;

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    protected Object createInstance() {
        log.info("[代理工厂] 初始化代理Bean : {}", interfaceClass);
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        final MethodInvokeMeta meta = WrapMethodUtils.readMethod(interfaceClass, method, args);
        if (!meta.getMethodName().equals("toString")) {
            log.info("[invoke] 调用接口{}, 调用方法名：{}, 参数类型：{} ,返回值类型{}",
                    meta.getInterfaceClass(),
                    meta.getMethodName(),
                    meta.getParameterTypes(),
                    meta.getReturnType());
        }
        return new NettyClient(nettyClient.getUrl(),nettyClient.getPort()).remoteCall(meta);
//        return nettyClient.remoteCall(meta);
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }
}
