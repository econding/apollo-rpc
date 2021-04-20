package com.apollo.rpc.core.concurrent;

import com.apollo.rpc.core.handler.holder.RequestMsgHolder;
import com.apollo.rpc.core.invocation.RPCMethodInvocation;
import com.apollo.rpc.core.msg.impl.RPCRequestMsg;
import com.apollo.rpc.core.msg.impl.RPCResponseMsg;
import com.apollo.rpc.core.exception.RPCException;

/**
 * 执行骨架
 */
public class RPCSkeleton implements Runnable{

    private RequestMsgHolder<RPCRequestMsg> requestMsgHolder;
    private RPCMethodInvocation invocation;

    public RPCSkeleton(RequestMsgHolder<RPCRequestMsg> requestMsgHolder, RPCMethodInvocation invocation){
        this.requestMsgHolder = requestMsgHolder;
        this.invocation = invocation;
    }

    public void run() {
        doService();
    }

    public void doService(){
        RPCResponseMsg responseMsg = requestMsgHolder.getMsg().getRspMsg();
        Object res = null;
        try {
            res = invocation.invokeMethod(requestMsgHolder.getMsg());
        }catch (Exception e){
            responseMsg.responseCode = RPCException.RemoteMethodInvocationException;
            Exception rpcException = new Exception("Request processing failed, service"+requestMsgHolder.getMsg().instanceName,e);
            responseMsg.exception = rpcException;
        }
        responseMsg.responseParameter = res;
        requestMsgHolder.sendResponse(responseMsg);
    }

}
