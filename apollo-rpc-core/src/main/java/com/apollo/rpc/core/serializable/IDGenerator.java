package com.apollo.rpc.core.serializable;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {

    private static final AtomicLong atomicLong = new AtomicLong();

    public static long getID(){
        return atomicLong.incrementAndGet();
    }

    /**
     * 基于CAS操作的id生成器(考虑上限)
     * @return
     */
    public static long getIdWithCAS(){
        long curr;
        long next;
        while (true){
            curr = atomicLong.get();
            next = curr == Long.MAX_VALUE?0:curr+1;
            if(atomicLong.compareAndSet(curr,next)){
                return next;
            }
        }
    }

}
