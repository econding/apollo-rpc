package com.apollo.rpc.core.exception;

import com.apollo.rpc.core.msg.RPCRspBase;

public class ResponseCodeErrorException extends RPCException {

    public ResponseCodeErrorException(){

    }

    public ResponseCodeErrorException(RPCRspBase rpcRspBase){
        super("ResponseCodeError: serverInstance="+rpcRspBase.instanceName+" RspCode="+rpcRspBase.responseCode);
    }

}
