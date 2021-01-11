package com.apollo.rpc.session;

import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.session.executor.RequestExecutor;
import io.netty.channel.Channel;

public class DefaultRpcSession implements RpcSession{

    private Channel channel;

    public DefaultRpcSession(Channel channel){
        this.channel = channel;
    }

    @Override
    public <T extends RPCReqBase> Object doRequest(T t) {
        RequestExecutor<T> request = new RequestExecutor<>();
        return request.doRequest(t,channel);
    }

}
