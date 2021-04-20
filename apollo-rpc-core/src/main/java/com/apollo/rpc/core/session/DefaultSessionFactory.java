package com.apollo.rpc.core.session;

import io.netty.channel.Channel;

public class DefaultSessionFactory implements RpcSessionFactory{

    public static final RpcSessionFactory instance = new DefaultSessionFactory();

    public RpcSession createSession(Channel channel){
        return new DefaultRpcSession(channel);
    }

}
