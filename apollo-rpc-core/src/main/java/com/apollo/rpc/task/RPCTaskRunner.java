package com.apollo.rpc.task;

import com.apollo.rpc.comm.Constant;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RPCTaskRunner{

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Constant.pool_size);

    /**
     * 周期性调度
     * @param command
     * @param rate
     */
    public static void execute(RPCScheduledRunnable command,long rate) {
        Future future = executor.scheduleAtFixedRate(command,0,rate, TimeUnit.MILLISECONDS);
        command.setFuture(future);
    }

    /**
     * 普通调度
     * @param command
     */
    public static void execute(RPCScheduledRunnable command) {
        executor.execute(command);
    }

}
