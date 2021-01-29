package com.apollo.rpc.exception;

import com.apollo.rpc.msg.impl.RPCResponseMsg;

public class NoSuchServiceException extends RPCException{

    public NoSuchServiceException(){

    }

    public NoSuchServiceException(RPCResponseMsg rpcResponseMsg){
        super("NoSuchServiceException: serverInstance="+rpcResponseMsg.serverInterface+" Method="+rpcResponseMsg.serverMethod+" parameter"+rpcResponseMsg.responseParameter);
    }

}
