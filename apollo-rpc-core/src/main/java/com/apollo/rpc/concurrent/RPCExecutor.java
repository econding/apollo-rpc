package com.apollo.rpc.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RPCExecutor {

    private Executor rpcExecutor;

    public RPCExecutor(int nThreads){
        rpcExecutor = Executors.newFixedThreadPool(nThreads);
    }

    public void execute(Runnable runnable){
        rpcExecutor.execute(runnable);
    }

}
