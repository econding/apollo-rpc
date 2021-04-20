package com.apollo.rpc.core.task;

import java.util.concurrent.Future;

public abstract class RPCScheduledRunnable implements Runnable{

    private Future future = null;

    public void setFuture(Future future) {
        this.future = future;
    }

    /**
     * 取消任务
     */
    public void cancel(){
        future.cancel(true);//中断当前任务
    }

}
