package com.apollo.rpc.concurrent;

/**
 * 令牌桶限流器
 */
public class TokenBucketLimiter {

    private int permitsProSecond = 0;
    private int currentSize = 0;
    private long lastAcquireTime = 0;

    public TokenBucketLimiter(int permitsProSecond){
        this.permitsProSecond = permitsProSecond;
        this.currentSize = permitsProSecond;
        this.lastAcquireTime = System.currentTimeMillis();
    }

    public boolean replenish(long micros){
        int increment = (int)micros/1000*permitsProSecond;
        if(increment == 0){
            return false;
        }
        currentSize += increment;
        return true;
    }

    public boolean tryAcquire(){
        long currTime = System.currentTimeMillis();
        if(hasPermit()){
            synchronized (this){
                if(hasPermit()){
                    currentSize--;
                    return true;
                }
            }
        }
        synchronized (this){
            if(replenish(currTime-lastAcquireTime)){
                lastAcquireTime = currTime;
            }
        }
        return false;
    }

    private boolean hasPermit(){
        return currentSize > 0;
    }

}
