package com.apollo.rpc.handler;

import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.handler.holder.RequestMsgHolder;
import com.apollo.rpc.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.service.AuthenticationService;

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
