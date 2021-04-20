package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.impl.RPCResponseMsg;

public class NoSuchServiceException extends RPCException{

    public NoSuchServiceException(){

    }

    public NoSuchServiceException(RPCResponseMsg rpcResponseMsg){
        super("NoSuchServiceException: serverInstance="+rpcResponseMsg.serverInterface+" Method="+rpcResponseMsg.serverMethod+" parameter"+rpcResponseMsg.responseParameter);
    }

}
