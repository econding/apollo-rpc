package com.apollo.rpc.core.comm;

public class CommonUtil {

    /**
     * 使用递归保证充足的睡眠时间
     * @param time
     */
    public static void sleep(long time){
        long last = System.currentTimeMillis();
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            sleep(System.currentTimeMillis() - last);
        }
    }

}
