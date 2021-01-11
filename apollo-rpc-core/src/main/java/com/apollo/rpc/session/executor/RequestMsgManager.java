package com.apollo.rpc.session.executor;


import com.apollo.rpc.exception.RPCExceptionUtil;
import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.msg.RPCRspBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMsgManager {

    private static final Map<String, Map<Long, RequestExecutor<RPCReqBase>>> sessions = new ConcurrentHashMap<>();

    private static Thread thread;

    public synchronized static void initialize(int rpc_client_timeout){

        if(thread == null){

            thread = new Thread(() -> {

                while(true){
                    long currTime = System.currentTimeMillis();
                    for (Map<Long, RequestExecutor<RPCReqBase>> map : sessions.values()) {
                        //避免ConcurrentModificationException，先将超时的请求提出来
                        List<RequestExecutor<RPCReqBase>> list = new ArrayList<>();
                        for (RequestExecutor<RPCReqBase> request : map.values()){
                            if(currTime - request.reqBase.requestTime > rpc_client_timeout){
                                list.add(request);
                            }
                        }
                        //执行超时操作
                        for(RequestExecutor request : list) {
                            doResponseOutOfTime(request);
                        }
                    }
                    try {
                        Thread.sleep(rpc_client_timeout/4);
                    } catch (InterruptedException e) {

                    }
                }
            });
            thread.start();
        }
    }

    /**
     * 虽然本方法执行的操作是非原子操作并且没有加锁，但是因为第一步操作(原子的)与第二步操作之间不存在约束性条件
     * (第一步操作的结果是确定的，所以不会影响第二步执行的结果)，所以本方法仍然是线程安全的
     * @param request
     * @param <A>
     */
    protected static <A extends RPCReqBase> void putRequest(RequestExecutor<A> request) {

        RequestExecutor<RPCReqBase> requestExecutor = (RequestExecutor<RPCReqBase>)request; //强制转型

        //第一步：获取对应服务的map
        Map<Long, RequestExecutor<RPCReqBase>> map = sessions.get(request.reqBase.serverName);

        if(map == null){
            synchronized (RequestMsgManager.class){
                map = sessions.get(request.reqBase.serverName);
                if(map == null){
                    map = createMap();
                    sessions.put(request.reqBase.serverName,map);
                }
            }
        }
        //第二步：将request放入map
        map.put(request.reqBase.sequenceNo, requestExecutor);

    }

    /**
     * 此方法跟{putRequest}类似，不需要同步仍然是线程安全的
     * @param response
     * @return
     */
    protected static RequestExecutor<RPCReqBase> getAndRemoveRequest(RPCRspBase response){

        Map<Long, RequestExecutor<RPCReqBase>> map = sessions.get(response.serverName);
        if(map != null){
            return map.remove(response.sequenceNo);
        }
        return null;
    }

    private synchronized static Map<Long, RequestExecutor<RPCReqBase>> createMap(){
        return new ConcurrentHashMap<>();
    }

    private static void doResponseOutOfTime(RequestExecutor request){

        try {
            RPCRspBase rspBase = request.reqBase.getRspMsg();
            rspBase.responseCode = RPCExceptionUtil.RequestOutOfTimeException;

            ResponseExecutor response = new ResponseExecutor();
            response.doResponse(rspBase);

            getAndRemoveRequest(rspBase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
