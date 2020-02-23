package com.sq.transportmanage.gateway.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtils.class);
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_SIZE = 3 * CORE_SIZE;
    private static final int LARGEST_SIZE = 200;
    private static final int QUEUE_SIZE = 20000;
    private static final int KEEP_ALIVE_TIME = 60;

    private static volatile ThreadPoolExecutor threadPoolExecutor = null;
    private static volatile ExecutorService singleThreadExecutor = null;
    private static Object singleThreadExecutorSync = new Object();



    /**
     * 优先使用队列
     *
     * @return
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (null == threadPoolExecutor) {
            synchronized (ThreadUtils.class) {
                if (null == threadPoolExecutor) {
                    int maxSize = MAX_SIZE;
                    if (MAX_SIZE > LARGEST_SIZE) {
                        maxSize = LARGEST_SIZE;
                    }
                    threadPoolExecutor = new ThreadPoolExecutor(CORE_SIZE, maxSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(QUEUE_SIZE), new NameThreadFactory("threadCommon"));
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> shutDown()));
                }
            }
        }
        return threadPoolExecutor;

    }

    public static void shutDown() {
        LOGGER.info("关闭线程池...");
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    private static void shutDownSingleThread() {
        LOGGER.info("关闭single线程池");
        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdown();
        }
    }

}