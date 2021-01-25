package com.apollo.rpc.factory;

import com.apollo.rpc.annotation.RpcClient;
import com.apollo.rpc.proxy.RPCProxyFactory;
import org.springframework.beans.factory.FactoryBean;

class RPCProxyFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaces;

    public RPCProxyFactoryBean(Class<T> interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public T getObject() {
        String serverName = interfaces.getAnnotation(RpcClient.class).value();
        if(serverName == null || serverName.length() == 0){
            throw new NullPointerException("serverName must not be null");
        }
        return RPCProxyFactory.getProxy(interfaces,serverName);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaces;
    }

}
