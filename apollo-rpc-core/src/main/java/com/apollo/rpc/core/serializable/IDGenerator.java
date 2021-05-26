package com.apollo.rpc.core.serializable;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {

    private static final AtomicLong atomicLong = new AtomicLong();

    public static long getID(){
        return atomicLong.incrementAndGet();
    }

}
