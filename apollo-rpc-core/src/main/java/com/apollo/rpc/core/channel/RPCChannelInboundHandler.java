package com.apollo.rpc.core.channel;

import com.apollo.rpc.core.handler.holder.MsgHolder;
import com.apollo.rpc.core.handler.holder.RequestMsgHolder;
import com.apollo.rpc.core.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.msg.RPCRspBase;
import com.apollo.rpc.core.service.RPCDispatchService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class RPCChannelInboundHandler extends SimpleChannelInboundHandler<Serializable> {

    private static final Log log = LogFactory.getLog(RPCChannelInboundHandler.class);

    private static RPCDispatchService dispatchService;

    public static void setDispatchService(RPCDispatchService rpcDispatchService) {
        dispatchService = rpcDispatchService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) {
        MsgHolder msgHolder = getRequest(msg,ctx.channel());
        if(msgHolder != null){
            dispatchService.dispatch(msgHolder);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("unchecked exception in ChannelHandler",cause);
    }

    public MsgHolder getRequest(Serializable msg, Channel channel){
        if(msg instanceof RPCReqBase){
            RPCReqBase rpcReqBase = (RPCReqBase)msg;
            return new RequestMsgHolder(rpcReqBase,channel);
        }else if(msg instanceof RPCRspBase) {
            RPCRspBase rpcRspBase = (RPCRspBase)msg;
            return new ResponseMsgHolder(rpcRspBase,channel);
        }
        log.debug("unsupported message type: " + msg.getClass().getName());
        return null;
    }

}
