package com.apollo.rpc.core.concurrent;

import com.apollo.rpc.core.comm.RPCProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RPCExecutorService {

    private Executor rpcExecutor;

    private static final int Default_Permit_Per_Second = 5000;
    private static final int Default_Permit_Max = 10000;
    private static final int Default_Thread_Size = 10;

    private TokenBucketLimiter limiter;

    public void init(RPCProperties properties){
        int pps = properties.getInt(RPCProperties.permit_per_second, Default_Permit_Per_Second);
        int ts = properties.getInt(RPCProperties.pool_size,Default_Thread_Size);
        int mp = properties.getInt(RPCProperties.max_permits, Default_Permit_Max);

        limiter = new TokenBucketLimiter(pps,mp);
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
