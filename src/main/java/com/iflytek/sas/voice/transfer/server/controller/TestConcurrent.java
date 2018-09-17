package com.iflytek.sas.voice.transfer.server.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: JiangPing Li
 * @date: 2018-09-13 9:52
 */
public class TestConcurrent {

    public AtomicInteger incAtomic = new AtomicInteger();

    public void increaseAtomic() {
        incAtomic.getAndIncrement();
    }

    public int inc = 0;

    public synchronized void increase() {
        inc++;
    }

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public void increaseLock() {
        lock.writeLock().lock();
        try{
            inc ++;
        }finally {
            lock.writeLock().unlock();
        }
    }

    private CountDownLatch latch = new CountDownLatch(20);

    public static void main(String[] args) throws InterruptedException {
        TestConcurrent test = new TestConcurrent();
        long start = System.currentTimeMillis();
        for(int i=0;i<20;i++){
            Thread thread = new Thread(){
                public void run() {
                    for(int j=0;j<100000;j++){
                        test.increaseLock();
                    }
                    test.latch.countDown();
                }
            };
            thread.start();
        }
        test.latch.await();
        long end = System.currentTimeMillis();
        char c = '汉';
        byte b = 100;

        String str = Byte.toString(b);
        str.getBytes();
        Character character = new Character(c);
        System.out.println(test.inc + "\ntime:" + (end-start) + c);
    }

}
