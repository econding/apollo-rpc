package com.apollo.rpc.core.exception;


public class NoSuchRemoteServerException extends RPCException {

    public NoSuchRemoteServerException(){

    }

    public NoSuchRemoteServerException(String serverName){
        super(" NoSuchRemoteServer: serverName="+serverName);
    }

}
