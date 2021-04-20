package com.apollo.rpc.core.session.executor;

import com.apollo.rpc.core.msg.RPCRspBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseExecutor<T extends RPCRspBase> {

    Log log = LogFactory.getLog(ResponseExecutor.class);

    public void doResponse(T  rspBase){

        RequestExecutor request = RequestMsgManager.getAndRemoveRequest(rspBase);

        rspBase.responseTime = System.currentTimeMillis();

        if(request != null){
            request.reqBase.rspBase = rspBase;
            synchronized (request.reqBase){ //唤醒等待的线程
                request.reqBase.notifyAll();
            }
        }else{
            log.error("Request packet missing, unable to wake up blocked thread: "+rspBase.instanceName);
        }
    }
}
