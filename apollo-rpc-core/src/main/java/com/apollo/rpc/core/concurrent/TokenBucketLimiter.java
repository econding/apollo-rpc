package com.apollo.rpc.core.concurrent;

/**
 * 令牌桶限流器
 */
public class TokenBucketLimiter {

    private int maxPermits = 0;
    private int permitsProSecond = 0;
    private int currentSize = 0;
    private long lastAcquireTime = 0;

    public TokenBucketLimiter(int permitsProSecond,int maxPermits){
        this.maxPermits = maxPermits;
        this.permitsProSecond = permitsProSecond;
        this.currentSize = permitsProSecond;
        this.lastAcquireTime = System.currentTimeMillis();
    }

    public boolean replenish(long micros){
        int increment = (int)micros*permitsProSecond/1000;
        if(increment == 0){
            return false;
        }
        currentSize = currentSize>=maxPermits?currentSize:currentSize+increment;
        return true;
    }

    public boolean tryAcquire(){
        boolean result = false;
        long currTime = System.currentTimeMillis();
        if(hasPermit()){
            synchronized (this){
                if(hasPermit()){
                    currentSize--;
                    result = true;
                }
            }
        }
        synchronized (this){
            if(replenish(currTime-lastAcquireTime)){
                lastAcquireTime = currTime;
            }
        }
        return result;
    }

    private boolean hasPermit(){
        return currentSize > 0;
    }

}
