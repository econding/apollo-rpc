package com.apollo.rpc.exception;

import com.apollo.rpc.msg.RPCRspBase;

public class ResponseOutOfTimeException extends RPCException {

    public ResponseOutOfTimeException(){

    }

    public ResponseOutOfTimeException(RPCRspBase rpcRspBase){

        super("ResponseOutOfTime: serverInstance="+rpcRspBase.instanceName);
    }

}
