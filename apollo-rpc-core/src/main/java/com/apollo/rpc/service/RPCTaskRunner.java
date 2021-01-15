package com.apollo.rpc.service;

import com.apollo.rpc.comm.Constant;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RPCTaskRunner{

    private static final Executor executor = Executors.newFixedThreadPool(Constant.pool_size);

    public static void execute(Runnable command) {
        executor.execute(command);
    }

}
