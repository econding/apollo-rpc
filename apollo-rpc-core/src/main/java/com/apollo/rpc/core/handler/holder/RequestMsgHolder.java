package com.apollo.rpc.core.handler.holder;

import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.msg.RPCRspBase;
import io.netty.channel.Channel;

public class RequestMsgHolder<M extends RPCReqBase> implements MsgHolder<M>{

    private M msg;
    private Channel channel;

    public RequestMsgHolder(M msg, Channel channel){
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

    /**
     * 返回一般消息
     * @param rspBase
     */
    public void sendResponse(RPCRspBase rspBase){
        channel.writeAndFlush(rspBase);
    }

    /**
     * 返回错误消息
     * @param responseCode
     */
    public void sendResponse(int responseCode){
        RPCRspBase rpcRspBase = msg.getRspMsg();
        rpcRspBase.responseCode = responseCode;
        sendResponse(rpcRspBase);
    }

}
