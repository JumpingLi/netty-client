package com.iflytek.sas.voice.transfer.server.controller;

import com.iflytek.sas.voice.transfer.server.rpc.util.ThreadPoolUtils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: JiangPing Li
 * @date: 2018-09-13 16:53
 */
public class TestQueue {
//    private static Logger logger = LoggerFactory.getLogger(TestQueue.class);
    private static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private static int count = 9;
    private static CountDownLatch latch = new CountDownLatch(count);

    public static void main(String[] args) throws InterruptedException {
        long timeStart = System.currentTimeMillis();
        ExecutorService es = ThreadPoolUtils.getExecutorService("my-test-thread-%d");
        TestQueue.offer();
        for (int i = 0; i < count; i++) {
            es.submit(new Poll());
        }
        latch.await();
        System.out.println("cost time " + (System.currentTimeMillis() - timeStart) + "ms");
        es.shutdown();
    }

    /**
     * 生产
     */
    public static void offer() {
        for (int i = 0; i < 1000; i++) {
            queue.offer(i);
        }
    }


    /**
     * 消费
     *
     * @author 林计钦
     * @version 1.0 2013-7-25 下午05:32:56
     */
    static class Poll implements Runnable {
        public void run() {
            // while (queue.size()>0) {
            while (!queue.isEmpty()) {
                System.out.println(queue.poll());
            }
            latch.countDown();
        }
    }
}
