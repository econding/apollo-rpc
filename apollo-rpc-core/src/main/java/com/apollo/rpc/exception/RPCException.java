package com.apollo.rpc.exception;

public class RPCException extends RuntimeException{

    public RPCException(){

    }

    public RPCException(String msg){
        super(msg);
    }

}
