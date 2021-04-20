package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.RPCRspBase;

public class NotAuthorizedException extends RPCException {

    public NotAuthorizedException(){

    }

    public NotAuthorizedException(RPCRspBase rpcRspBase){
        super("NotAuthorized: instanceName="+rpcRspBase.instanceName);
    }

}
