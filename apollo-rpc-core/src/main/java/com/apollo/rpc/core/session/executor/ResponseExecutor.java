package com.apollo.rpc.core.session.executor;

import com.apollo.rpc.core.msg.RPCRspBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseExecutor<T extends RPCRspBase> {

    private static final Log log = LogFactory.getLog(ResponseExecutor.class);

    public void doResponse(T  rspBase){

        RequestExecutor request = RequestMsgManager.getAndRemoveRequest(rspBase);

        rspBase.responseTime = System.currentTimeMillis();

        if(request != null){
            request.reqBase.rspBase = rspBase;
            request.weakUp();
        }else{
            log.error("Request packet missing, unable to wake up blocked thread: "+rspBase.sequenceNo);
        }

    }
}
