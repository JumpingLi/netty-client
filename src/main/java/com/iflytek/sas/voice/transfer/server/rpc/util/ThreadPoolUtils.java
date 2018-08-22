package com.iflytek.sas.voice.transfer.server.rpc.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 *
 * @author jpli3
 */
@Slf4j
public class ThreadPoolUtils {
    public static ExecutorService getExecutorService(String s) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(s).build();
        int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        log.warn("---线程池数量: " + corePoolSize);
        return new ThreadPoolExecutor(corePoolSize, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(128), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
