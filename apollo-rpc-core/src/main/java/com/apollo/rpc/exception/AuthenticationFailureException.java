package com.apollo.rpc.exception;

import com.apollo.rpc.msg.impl.RPCAuthRspMsg;

public class AuthenticationFailureException extends RPCException {

    public AuthenticationFailureException(){

    }

    public AuthenticationFailureException(RPCAuthRspMsg rpcAuthRspMsg){
        super("AuthenticationFailure: serviceInstance="+rpcAuthRspMsg.instanceName);
    }

}
