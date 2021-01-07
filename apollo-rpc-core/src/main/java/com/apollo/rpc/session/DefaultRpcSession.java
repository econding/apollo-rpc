package com.apollo.rpc.session;

import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.session.excutor.RequestExcutor;
import io.netty.channel.Channel;

public class DefaultRpcSession implements RpcSession{

    private Channel channel;

    public DefaultRpcSession(Channel channel){
        this.channel = channel;
    }

    @Override
    public <T extends RPCReqBase> Object doRequest(T t) {
        RequestExcutor<T> request = new RequestExcutor<>();
        return request.doRequest(t,channel);
    }

}
