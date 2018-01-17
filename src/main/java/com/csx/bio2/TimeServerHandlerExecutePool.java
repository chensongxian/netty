package com.csx.bio2;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018-01-17
 */
public class TimeServerHandlerExecutePool {
    private ExecutorService executor;

    public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize) {
        executor= new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()
        ,maxPoolSize
        ,120L
        ,TimeUnit.SECONDS
        ,new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void exec(Runnable task){
        executor.execute(task);
    }
}
