package com.apollo.rpc.core.session.executor;

import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.msg.RPCRspBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务消息管理
 */
public class RemoteServerMsgCache {

    private static final Log log = LogFactory.getLog(RemoteServerMsgCache.class);
    private int time_out;
    private String serverName;
    private Map<Long,RequestExecutor> msgCache;

    public RemoteServerMsgCache(String serverName,int time_out){
        this.time_out = time_out;
        this.serverName = serverName;
        this.msgCache = new ConcurrentHashMap();
    }

    public void put(long seqNo,RequestExecutor request){
        msgCache.put(seqNo,request);
    }

    public RequestExecutor remove(long seqNo){
        return msgCache.remove(seqNo);
    }

    public boolean isEmpty(){
        return msgCache.isEmpty();
    }


    public void check(long currTime){
        Iterator<Map.Entry<Long,RequestExecutor>> iterable = msgCache.entrySet().iterator();
        while(iterable.hasNext()){
            Map.Entry<Long,RequestExecutor> entry = iterable.next();
            if(currTime - entry.getValue().reqBase.requestTime > time_out){ //判断超时
                doResponseOutOfTime(entry.getValue());
                iterable.remove();
            }
        }
    }

    /*
    public void check(long currTime){
        List<Long> longList = new ArrayList<>();
        for(long seqNo:msgCache.keySet()){
            RequestExecutor request = msgCache.get(seqNo);
            if(request != null){
                if(currTime - request.reqBase.requestTime > time_out){
                    doResponseOutOfTime(request);
                    longList.add(seqNo);
                }
            }
        }
        synchronized (this){
            for(long seqNo:longList){
                msgCache.remove(seqNo);
            }
        }
    }*/

    private void doResponseOutOfTime(RequestExecutor request){
        log.info("response out of time seq: "+request.reqBase.serverName+" "+request.reqBase.sequenceNo);
        RPCRspBase rspBase = request.reqBase.getRspMsg();
        rspBase.responseCode = RPCException.RequestOutOfTimeException;
        request.reqBase.rspBase = rspBase;
        request.weakUp();
    }

}
