package com.apollo.rpc.client.proxy;

import java.lang.reflect.Proxy;

public class RPCProxyFactory {

    public static <T> T getProxy(Class<T> interfaces, String serverName){
        RPCProxy proxy = new RPCProxy<>(interfaces,serverName);
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(),new Class[]{interfaces},proxy);
    }

}
