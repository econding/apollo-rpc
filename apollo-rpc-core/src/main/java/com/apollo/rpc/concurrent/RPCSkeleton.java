package com.apollo.rpc.concurrent;

import com.apollo.rpc.exception.RPCExceptionUtil;
import com.apollo.rpc.handler.holder.RequestMsgHolder;
import com.apollo.rpc.invocation.RPCMethodInvocation;
import com.apollo.rpc.msg.impl.RPCRequestMsg;
import com.apollo.rpc.msg.impl.RPCResponseMsg;

import java.lang.reflect.InvocationTargetException;

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
        Object res = null;
        int responseCode = 0;
        try {
            res = invocation.invokeMethod(requestMsgHolder.getMsg());
        } catch (IllegalAccessException e) {
            responseCode = RPCExceptionUtil.IllegalAccessException;
        } catch (InvocationTargetException e) {
            responseCode = RPCExceptionUtil.RpcInvocationTargetException;
        } catch (NoSuchMethodException e) {
            responseCode = RPCExceptionUtil.NoSuchMethodException;
        }
        RPCResponseMsg responseMsg = requestMsgHolder.getMsg().getRspMsg();
        responseMsg.responseParameter = res;
        responseMsg.responseCode = responseCode;
        requestMsgHolder.sendResponse(responseMsg);
    }

}
