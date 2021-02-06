package com.apollo.rpc.proxy;

import com.apollo.rpc.exception.NoSuchRemoteServerException;
import com.apollo.rpc.remote.RemoteServerHolder;
import com.apollo.rpc.remote.server.RemoteServer;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Rpc客户端代理
 * @param <T>
 */
public class RPCProxy<T> implements InvocationHandler,Serializable{

    private static final long serialVersionUID = -6424540698551729830L;
    private static RemoteServerHolder remoteServerHolder;

    private final Class<T> rpcInterface;
    private final String serverName ;

    public RPCProxy(Class<T> rpcInterface, String serverName){
        this.rpcInterface = rpcInterface;
        this.serverName = serverName;
    }

    public static void setRemoteServerHolder(RemoteServerHolder remoteServerHolder) {
        RPCProxy.remoteServerHolder = remoteServerHolder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        RemoteServer remoteServer = remoteServerHolder.getRemoteServer(serverName);
        if(remoteServer != null){
            return remoteServer.invoke(rpcInterface.getSimpleName(),method.getName(),args);
        }else{
            throw new NoSuchRemoteServerException(serverName);
        }
    }

}
