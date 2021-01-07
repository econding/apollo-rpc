package com.apollo.rpc.handler.holder;

import com.apollo.rpc.msg.RPCRspBase;
import io.netty.channel.Channel;

public class ResponseMsgHolder<M extends RPCRspBase> implements MsgHolder<M>{

    private M msg;
    private Channel channel;

    public ResponseMsgHolder(M msg, Channel channel){
        this.msg = msg;
        this.channel = channel;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public M getMsg() {
        return msg;
    }
}
