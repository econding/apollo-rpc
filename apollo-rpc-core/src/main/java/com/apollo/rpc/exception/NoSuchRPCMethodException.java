package com.apollo.rpc.exception;

import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class NoSuchRPCMethodException extends RPCException {

    public NoSuchRPCMethodException(){

    }

    public NoSuchRPCMethodException(RPCResponseMsg rpcResponseMsg){
        super("RpcInvocationTargetException: serverInstace="+rpcResponseMsg.serverInterface+" Method="+rpcResponseMsg.serverMethod+" parameter"+rpcResponseMsg.responseParameter);
    }

}
