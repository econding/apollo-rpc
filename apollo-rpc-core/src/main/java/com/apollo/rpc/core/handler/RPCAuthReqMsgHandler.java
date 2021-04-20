package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.service.AuthenticationService;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.handler.holder.RequestMsgHolder;
import com.apollo.rpc.core.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.core.msg.impl.RPCAuthRspMsg;

public class RPCAuthReqMsgHandler implements RPCMsgHandler<RequestMsgHolder<RPCAuthReqMsg>,RPCAuthReqMsg> {

    private AuthenticationService authenticationService;

    public void initHandler(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    public void doHandle(RequestMsgHolder<RPCAuthReqMsg> requestMsgHolder) {
        boolean authSuccess = authenticationService.authenticate(requestMsgHolder.getChannel(), requestMsgHolder.getMsg());
        RPCAuthRspMsg rspMsg = requestMsgHolder.getMsg().getRspMsg();
        if(!authSuccess){
            rspMsg.responseCode = RPCException.AuthenticationFailureException;
        }
        rspMsg.result = authSuccess;
        requestMsgHolder.sendResponse(rspMsg);
    }

}
