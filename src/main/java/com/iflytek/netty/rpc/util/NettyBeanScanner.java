package com.iflytek.netty.rpc.util;

import com.iflytek.netty.client.RpcProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author jpli3
 */
public class NettyBeanScanner implements BeanFactoryPostProcessor {

    private DefaultListableBeanFactory beanFactory;

    private String basePackage;

    private String clientName;

    public NettyBeanScanner(String basePackage, String clientName) {
        this.basePackage = basePackage;
        this.clientName = clientName;
    }


    /**
     * 注册Bean到Spring的bean工厂
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        // 加载远程服务的接口
        List<String> resolverClass =
//                PackageClassUtils.resolver(basePackage);
        Collections.singletonList("com.iflytek.netty.rpc.service.DemoService");
        for (String clazz : resolverClass) {
            String simpleName;
            if (clazz.lastIndexOf('.') != -1) {
                simpleName = clazz.substring(clazz.lastIndexOf('.') + 1);
            } else {
                simpleName = clazz;
            }
            BeanDefinitionBuilder gd = BeanDefinitionBuilder.genericBeanDefinition(RpcProxyFactoryBean.class);
            gd.addPropertyValue("interfaceClass", clazz);
            gd.addPropertyReference("nettyClient", clientName);
            this.beanFactory.registerBeanDefinition(simpleName, gd.getRawBeanDefinition());
        }
    }
}
