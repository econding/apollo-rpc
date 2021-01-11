package com.apollo.rpc.session.executor;


import com.apollo.rpc.exception.RemoteServerDisabledException;
import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.exception.RPCExceptionUtil;
import com.apollo.rpc.msg.RPCReqBase;
import io.netty.channel.Channel;


public class RequestExecutor<A extends RPCReqBase> extends RequestMsgManager {

    public A reqBase;

    public Object doRequest(A reqBase, Channel channel) throws RPCException {

        this.reqBase = reqBase;

        if(channel != null){

            channel.writeAndFlush(reqBase);

            putRequest(this);   //存储请求

            waiting(reqBase);

            if(reqBase.rspBase.responseCode != 0){
                throw RPCExceptionUtil.throwException(reqBase.rspBase);
            }

        }else{
            throw new RemoteServerDisabledException();
        }
        return reqBase.rspBase.responseParameter;

    }

    /**
     * 使用方法递归保证不会产生中断异常，导致请求发出后，在收到应答时无法正确处理
     * @param o
     */
    private void waiting(Object o){
        synchronized (o){ //在请求上做同步
            try {
                o.wait();
            } catch (InterruptedException e) {
                waiting(o);
            }
        }
    }
}

