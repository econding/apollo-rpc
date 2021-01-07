package com.apollo.rpc.session.excutor;

import com.apollo.rpc.msg.RPCRspBase;

public class ResponseExcutor<T extends RPCRspBase> extends RequestMsgManager {

    public void doResponse(T  rspBase){

        RequestExcutor request = getAndRemoveRequest(rspBase);

        rspBase.responseTime = System.currentTimeMillis();

        if(request != null){
            request.reqBase.rspBase = rspBase;
            synchronized (request.reqBase){ //唤醒等待的线程
                request.reqBase.notifyAll();
            }
        }
    }
}
