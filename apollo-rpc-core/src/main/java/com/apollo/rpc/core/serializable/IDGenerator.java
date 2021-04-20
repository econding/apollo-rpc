package com.apollo.rpc.core.serializable;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {

    private static AtomicLong atomicLong = new AtomicLong();

    public static synchronized long getID(){
        if(atomicLong.compareAndSet(Long.MAX_VALUE,0)){
            return 0;
        }
        return atomicLong.incrementAndGet();
    }

}
