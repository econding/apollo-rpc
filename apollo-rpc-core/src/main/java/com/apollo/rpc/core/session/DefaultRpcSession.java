package com.apollo.rpc.core.session;

import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.session.executor.RequestExecutor;
import io.netty.channel.Channel;

public class DefaultRpcSession implements RpcSession{

    private Channel channel;

    public DefaultRpcSession(Channel channel){
        this.channel = channel;
    }

    @Override
    public <T extends RPCReqBase> Object request(T t) {
        RequestExecutor<T> request = new RequestExecutor<>();
        return request.doRequest(t,channel);
    }

}
