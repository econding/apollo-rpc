package com.apollo.rpc.core.remote.server;

public interface RemoteServer{

    String getServername();

    Object invoke(String className, String method, Object[] args);

}
