package com.apollo.rpc.core.session;

import io.netty.channel.Channel;

public interface RpcSessionFactory {

    RpcSession createSession(Channel channel);

}
