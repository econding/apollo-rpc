package com.apollo.rpc.core.session.executor;

import com.apollo.rpc.core.comm.CommonUtil;
import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.msg.RPCRspBase;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.task.RPCScheduledRunnable;
import com.apollo.rpc.core.task.RPCTaskScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

/**
 * 请求报文的管理类
 * 线程安全类
 */
public class RequestMsgManager {

    private static final Log log = LogFactory.getLog(RequestMsgManager.class);
    private static final Map<String, RemoteServerMsgCache> caches = new ConcurrentHashMap<>();
    private static Thread thread;
    private static int time_out;
    private static StampedLock stampedLock = new StampedLock();//高性能读写锁


    public synchronized static void initialize(int rpc_client_timeout){
        time_out = rpc_client_timeout;
        if(thread == null){
            thread = new Thread(() -> {   //使用线程而不使用定时调度任务
                while(true){
                    check();
                    CommonUtil.sleep(time_out/4);
                }
            });
            thread.start();
        }
    }

    private synchronized static void check(){
        long currTime = System.currentTimeMillis();
        for (RemoteServerMsgCache msgCache : caches.values()) {
            msgCache.check(currTime);
        }
    }

    public synchronized static void createCacheMapForNewServer(String serverName){
        long temp = stampedLock.writeLock();
        try {
            RemoteServerMsgCache map = caches.get(serverName);
            if(map == null){
                caches.put(serverName,new RemoteServerMsgCache(serverName,time_out));
            }
        }finally {
            stampedLock.unlock(temp);
        }
    }

    public synchronized static void removeCacheMap(String serverName){
        RemoteServerMsgCache cache = caches.get(serverName);
        if(cache != null){
            RPCTaskScheduler.schedule(new RPCScheduledRunnable() {
                @Override
                public void run() {
                    if(cache.isEmpty()){  //等待map清空后再移除
                        long temp = stampedLock.writeLock();
                        try {
                            caches.remove(serverName);
                        }finally {
                            stampedLock.unlock(temp);
                        }
                        cancel();
                    }
                }
            },100);
        }
    }

    public static void putRequest(RequestExecutor request) {
        //第一步：获取对应服务的map
        RemoteServerMsgCache map;
        long temp = stampedLock.readLock();
        try {
            map = caches.get(request.reqBase.serverName);
        }finally {
            stampedLock.unlock(temp);
        }
        if(map != null){
            //第二步：将request放入map
            map.put(request.reqBase.sequenceNo, request);
            //log.info("request seq: "+request.reqBase.serverName+" "+request.reqBase.sequenceNo);
        }else{
            log.error("server msg cache not found : serverName "+request.reqBase.serverName);
        }
    }

    public static RequestExecutor getAndRemoveRequest(RPCRspBase response){
        RemoteServerMsgCache map = caches.get(response.serverName);
        if(map != null){
            return map.remove(response.sequenceNo);
        }
        return null;
    }

}
