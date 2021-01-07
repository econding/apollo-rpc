package com.apollo.rpc.exception;

public class RemoteServerDisabledException extends RPCException {

    public RemoteServerDisabledException(){
    }

    public RemoteServerDisabledException(String serverName){
        super("RemoteServerDisabled: serverName="+serverName);
    }

}
