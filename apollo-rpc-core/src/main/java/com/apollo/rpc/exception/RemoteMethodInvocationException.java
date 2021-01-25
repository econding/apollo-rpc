package com.apollo.rpc.exception;

public class RemoteMethodInvocationException extends RPCException{

    public RemoteMethodInvocationException(){

    }

    public RemoteMethodInvocationException(String msg,Throwable cause){
        super(msg,cause);
    }

}
