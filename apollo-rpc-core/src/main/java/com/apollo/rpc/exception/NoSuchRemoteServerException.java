package com.apollo.rpc.exception;


public class NoSuchRemoteServerException extends RPCException {

    public NoSuchRemoteServerException(){

    }

    public NoSuchRemoteServerException(String serverName){
        super(" NoSuchRemoteServer: serverName="+serverName);
    }

}
