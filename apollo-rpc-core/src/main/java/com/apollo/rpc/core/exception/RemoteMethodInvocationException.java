package com.apollo.rpc.core.exception;

public class RemoteMethodInvocationException extends RPCException{

    public RemoteMethodInvocationException(){

    }

    public RemoteMethodInvocationException(String msg){
        super(msg);
    }

    public RemoteMethodInvocationException(String msg,Throwable cause){
        super(msg,cause);
    }

}
