package com.apollo.rpc.concurrent;

import com.apollo.rpc.comm.RPCProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RPCExecutorService {

    private Executor rpcExecutor;

    private static final int Default_Permit_Per_Second = 5000;
    private static final int Default_Thread_Size = 100;

    private TokenBucketLimiter limiter;

    public void init(RPCProperties properties){
        int pps = properties.getInt(RPCProperties.permit_per_second, Default_Permit_Per_Second);
        int ts = properties.getInt(RPCProperties.pool_size,Default_Thread_Size);

        limiter = new TokenBucketLimiter(pps);
        rpcExecutor = Executors.newFixedThreadPool(ts);
    }

    public boolean execute(Runnable runnable){
        if(limiter.tryAcquire()){
            rpcExecutor.execute(runnable);
            return true;
        }
        return false;
    }

}
