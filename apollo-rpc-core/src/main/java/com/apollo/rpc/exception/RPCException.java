package com.apollo.rpc.exception;

public class RPCException extends RuntimeException{

    public RPCException(){
        super();
    }

    public RPCException(Throwable cause){
        super(cause);
    }

    public RPCException(String msg,Throwable cause) {
        super(msg,cause);
    }

    public RPCException(String msg){
        super(msg);
    }

}
