package com.iflytek.netty.rpc.service;


import com.iflytek.netty.rpc.entity.TransportRequest;
import com.iflytek.netty.rpc.entity.TransportResponse;

/**
 *
 * @author jpli3
 */
public interface DemoService {

    /**
     * 求和方法
     *
     * @param numberA 第一个数
     * @param numberB 第二个数
     * @return 两数之和
     */
    int sum(int numberA, int numberB);

    /**
     * 打印方法
     *
     * @return 一个字符串
     */
    String print();

    /**
     *  文件传输
     * @param request
     * @return
     */
    TransportResponse fileTransport(TransportRequest request);

}
