package com.apollo.rpc.session;

import io.netty.channel.Channel;

public interface RpcSessionFactory {

    RpcSession createSession(Channel channel);

}
