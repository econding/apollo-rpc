package com.apollo.rpc.core.proxy;

import java.lang.reflect.Proxy;

public class RPCProxyFactory {

    /**
     * 为接口生成JDK动态代理
     * @param interfaces
     * @param serverName
     * @param <T>
     * @return
     */
    public static <T> T getProxy(Class<T> interfaces, String serverName){
        RPCInvocationHandler handler = new RPCInvocationHandler<>(interfaces,serverName);
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(),new Class[]{interfaces},handler);
    }

}
