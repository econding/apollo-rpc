package com.apollo.rpc.core.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 令牌桶限流器
 */
public class TokenBucketLimiter {

    private final int maxPermits;
    private final int permitsProSecond;
    private AtomicInteger currentSize;
    private long lastAcquireTime = 0;

    public TokenBucketLimiter(int permitsPerSecond,int maxPermits){
        this.maxPermits = maxPermits;
        this.permitsProSecond = permitsPerSecond;
        this.currentSize = new AtomicInteger(permitsProSecond);
        this.lastAcquireTime = System.currentTimeMillis();
    }

    public boolean replenish(long currTime){
        long micros = currTime - lastAcquireTime;
        int increment = (int)micros*permitsProSecond/1000;
        if(increment == 0){
            return false;
        }
        while(true){
            int currS = currentSize.get();
            int expect = Math.min(maxPermits,currS+increment);
            if(currentSize.compareAndSet(currS,expect)){
                this.lastAcquireTime = currTime;
                return true;
            }
        }
    }

    public boolean tryAcquire(){
        boolean result;
        long currTime = System.currentTimeMillis();
        replenish(currTime);
        while(true){
            int curr = currentSize.get();
            if(curr > 0){
                if(currentSize.compareAndSet(curr,curr-1)){
                    result = true;
                    break;
                }
            }else{
                result = false;
                break;
            }
        }
        return result;
    }

}
