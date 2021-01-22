package com.apollo.rpc.concurrent;

import com.apollo.rpc.exception.RPCExceptionUtil;
import com.apollo.rpc.handler.holder.RequestMsgHolder;
import com.apollo.rpc.invocation.RPCMethodInvocation;
import com.apollo.rpc.msg.impl.RPCRequestMsg;
import com.apollo.rpc.msg.impl.RPCResponseMsg;

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
        }  catch (Exception e){
            responseMsg.responseCode = RPCExceptionUtil.RemoteMethodInvocationException;
            responseMsg.exception = e;
        }
        responseMsg.responseParameter = res;
        requestMsgHolder.sendResponse(responseMsg);
    }

}
