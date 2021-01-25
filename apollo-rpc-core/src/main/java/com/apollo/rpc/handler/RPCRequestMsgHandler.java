package com.apollo.rpc.handler;

import com.apollo.rpc.concurrent.RPCExecutorService;
import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.handler.holder.RequestMsgHolder;
import com.apollo.rpc.concurrent.RPCSkeleton;
import com.apollo.rpc.invocation.RPCMethodInvocation;
import com.apollo.rpc.msg.impl.RPCRequestMsg;
import com.apollo.rpc.service.RPCServiceRegister;

public class RPCRequestMsgHandler implements RPCMsgHandler<RequestMsgHolder<RPCRequestMsg>,RPCRequestMsg> {

    private RPCExecutorService executorService;
    private RPCServiceRegister serviceRegister;

    public void initHandler(RPCExecutorService executorService, RPCServiceRegister register){
        this.executorService = executorService;
        this.serviceRegister = register;
    }

    @Override
    public void doHandle(RequestMsgHolder<RPCRequestMsg> requestMsgHolder) {
        RPCSkeleton rpcSkeleton = getRpcSkeleton(requestMsgHolder);
        if(rpcSkeleton == null){
            requestMsgHolder.sendResponse(RPCException.NoSuchServiceException);
            return;
        }
        boolean run = this.executorService.execute(rpcSkeleton);
        if(!run){
            requestMsgHolder.sendResponse(RPCException.RemoteServerLimitException);
        }
    }

    private RPCSkeleton getRpcSkeleton(RequestMsgHolder<RPCRequestMsg> requestMsgHolder){
        Object service = serviceRegister.getService(requestMsgHolder.getMsg().serverInterface);
        if(service != null){
            RPCMethodInvocation invocation = new RPCMethodInvocation(service);
            return new RPCSkeleton(requestMsgHolder,invocation);
        }
        return null;
    }

}
