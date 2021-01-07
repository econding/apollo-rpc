package com.apollo.rpc.initializer;

import com.apollo.rpc.handler.holder.MsgHolder;
import com.apollo.rpc.handler.holder.RequestMsgHolder;
import com.apollo.rpc.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.msg.RPCReqBase;
import com.apollo.rpc.msg.RPCRspBase;
import com.apollo.rpc.service.RPCDispatchService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class RPCChannelHandler extends SimpleChannelInboundHandler<Serializable> {

    private static final Log log = LogFactory.getLog(RPCChannelHandler.class);

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
