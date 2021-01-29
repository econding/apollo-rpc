package com.apollo.rpc.exception;

import com.apollo.rpc.msg.RPCRspBase;

public class RemoteServerLimitException extends RPCException {

    public RemoteServerLimitException(){

    }

    public RemoteServerLimitException(RPCRspBase rpcRspBase){
        super("RemoteServerLimit: serverInstance="+rpcRspBase.instanceName);
    }

}
