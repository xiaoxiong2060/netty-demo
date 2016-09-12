package com.xiong.netty.pio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerExecutePool {

    private Executor executor;

    public ServerExecutePool(int maxPoolSize, int queueSize){
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L,
                                               TimeUnit.SECONDS, new ArrayBlockingQueue<java.lang.Runnable>(queueSize));
    }
    
    public void execute(java.lang.Runnable task){
        executor.execute(task);
    }

}
