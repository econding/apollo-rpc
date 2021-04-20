package com.apollo.rpc.core.task;

import com.apollo.rpc.core.comm.Constant;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPCTaskScheduler {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Constant.pool_size);

    /**
     * 周期性调度
     * @param command 任务
     * @param rate 频率
     */
    public static void schedule(RPCScheduledRunnable command, long rate) {
        Future future = executor.scheduleAtFixedRate(command,0,rate, TimeUnit.MILLISECONDS);
        command.setFuture(future);
    }

    /**
     * 普通执行
     * @param command
     */
    public static void execute(RPCScheduledRunnable command) {
        executor.execute(command);
    }

}
