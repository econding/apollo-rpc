package com.apollo.rpc.remote.server;

public interface RemoteServer{

    String getServername();

    Object invoke(String ClassName, String Method, Object[] args);

}
