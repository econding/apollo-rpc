package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.concurrent.RPCExecutorService;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.handler.holder.RequestMsgHolder;
import com.apollo.rpc.core.msg.impl.RPCServerCheckReqMsg;

public class RPCServerCheckReqMsgHandler implements RPCMsgHandler<RequestMsgHolder<RPCServerCheckReqMsg>, RPCServerCheckReqMsg> {

    private RPCExecutorService executorService;

    public void initHandler(RPCExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    public void doHandle(RequestMsgHolder<RPCServerCheckReqMsg> requestMsgHolder) {
        Check check = new Check(requestMsgHolder);
        boolean run = this.executorService.execute(check);
        if(!run){
            requestMsgHolder.sendResponse(RPCException.RemoteServerLimitException);
        }
    }

    private class Check implements Runnable{

        private RequestMsgHolder<RPCServerCheckReqMsg> requestMsgHolder;

        public Check(RequestMsgHolder<RPCServerCheckReqMsg> requestMsgHolder){
            this.requestMsgHolder = requestMsgHolder;
        }

        @Override
        public void run() {
            requestMsgHolder.sendResponse(0);
        }

    }

}
