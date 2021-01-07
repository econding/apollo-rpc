package com.apollo.rpc.exception;

import com.apollo.rpc.msg.RPCRspBase;

public class NotAuthorizedException extends RPCException {

    public NotAuthorizedException(){

    }

    public NotAuthorizedException(RPCRspBase rpcRspBase){
        super("NotAuthorized: instanceName="+rpcRspBase.instanceName);
    }

}
