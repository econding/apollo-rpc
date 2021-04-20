package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.RPCRspBase;

public class ResponseOutOfTimeException extends RPCException {

    public ResponseOutOfTimeException(){

    }

    public ResponseOutOfTimeException(RPCRspBase rpcRspBase){

        super("ResponseOutOfTime: serverInstance="+rpcRspBase.instanceName);
    }

}
