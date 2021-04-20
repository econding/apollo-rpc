package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.concurrent.RPCExecutorService;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.handler.holder.RequestMsgHolder;
import com.apollo.rpc.core.concurrent.RPCSkeleton;
import com.apollo.rpc.core.invocation.RPCMethodInvocation;
import com.apollo.rpc.core.msg.impl.RPCRequestMsg;
import com.apollo.rpc.core.service.RPCServiceRegister;

public class RPCRequestMsgHandler implements RPCMsgHandler<RequestMsgHolder<RPCRequestMsg>,RPCRequestMsg> {

    private RPCExecutorService executorService;
    private RPCServiceRegister serviceRegister;

    public void initHandler(RPCExecutorService executorService, RPCServiceRegister register){
        this.executorService = executorService;
        this.serviceRegister = register;
    }

    @Override
    public void doHandle(RequestMsgHolder<RPCRequestMsg> requestMsgHolder) {
        RPCSkeleton rpcSkeleton = getRRCSkeleton(requestMsgHolder);
        if(rpcSkeleton == null){
            requestMsgHolder.sendResponse(RPCException.NoSuchServiceException);
            return;
        }
        boolean run = this.executorService.execute(rpcSkeleton);
        if(!run){
            requestMsgHolder.sendResponse(RPCException.RemoteServerLimitException);
        }
    }

    private RPCSkeleton getRRCSkeleton(RequestMsgHolder<RPCRequestMsg> requestMsgHolder){
        Object service = serviceRegister.getService(requestMsgHolder.getMsg().serverInterface);
        if(service != null){
            RPCMethodInvocation invocation = new RPCMethodInvocation(service);
            return new RPCSkeleton(requestMsgHolder,invocation);
        }
        return null;
    }

}
