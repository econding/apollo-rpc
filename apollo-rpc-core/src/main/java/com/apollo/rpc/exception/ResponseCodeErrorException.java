package com.apollo.rpc.exception;

import com.apollo.rpc.msg.RPCRspBase;

public class ResponseCodeErrorException extends RPCException {

    public ResponseCodeErrorException(){

    }

    public ResponseCodeErrorException(RPCRspBase rpcRspBase){
        super("ResponseCodeError: serverInstance="+rpcRspBase.instanceName+" RspCode="+rpcRspBase.responseCode);
    }

}
