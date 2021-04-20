package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.RPCRspBase;

public class RemoteServerLimitException extends RPCException {

    public RemoteServerLimitException(){

    }

    public RemoteServerLimitException(RPCRspBase rpcRspBase){
        super("RemoteServerLimit: serverInstance="+rpcRspBase.instanceName);
    }

}
