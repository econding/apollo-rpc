package com.apollo.rpc.remote.server;

import com.apollo.rpc.exception.RPCException;

public interface RemoteServer{

    String getServername();

    Object doRequest(String ClassName,String Method,Object[] args) throws RPCException;

}
