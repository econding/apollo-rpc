package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.impl.RPCAuthRspMsg;

public class AuthenticationFailureException extends RPCException {

    public AuthenticationFailureException(){

    }

    public AuthenticationFailureException(RPCAuthRspMsg rpcAuthRspMsg){
        super("AuthenticationFailure: serviceInstance="+rpcAuthRspMsg.instanceName);
    }

}
