package com.apollo.rpc.exception;

import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class RPCInvocationTargetException extends RPCException {

    public RPCInvocationTargetException(){

    }

    public RPCInvocationTargetException(RPCResponseMsg rpcResponseMsg){
        super("RpcInvocationTargetException: serverInstace="+rpcResponseMsg.serverInterface+" Method="+rpcResponseMsg.serverMethod+" parameter"+rpcResponseMsg.responseParameter);
    }

}
