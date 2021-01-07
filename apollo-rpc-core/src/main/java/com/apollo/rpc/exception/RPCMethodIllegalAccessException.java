package com.apollo.rpc.exception;

import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class RPCMethodIllegalAccessException extends RPCException {

    public RPCMethodIllegalAccessException(){

    }

    public RPCMethodIllegalAccessException(RPCResponseMsg rpcResponseMsg){
        super("RpcInvocationTargetException: serverInstace="+rpcResponseMsg.serverInterface+" Method="+rpcResponseMsg.serverMethod+" parameter"+rpcResponseMsg.responseParameter);
    }

}
