package com.apollo.rpc.core.session.executor;


import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.exception.RemoteServerDisabledException;
import com.apollo.rpc.core.exception.RPCException;
import io.netty.channel.Channel;


public class RequestExecutor<A extends RPCReqBase> {

    public A reqBase;

    public Object doRequest(A reqBase, Channel channel) {

        this.reqBase = reqBase;

        if(channel != null){

            channel.writeAndFlush(reqBase); //发送消息

            RequestMsgManager.putRequest(this);   //存储请求

            waiting(reqBase);   //等待

            if(reqBase.rspBase.responseCode != 0){
                throw RPCException.throwException(reqBase.rspBase); //异常处理
            }

        }else{
            throw new RemoteServerDisabledException(reqBase.serverName);
        }

        return reqBase.rspBase.responseParameter;

    }

    /**
     * 使用方法递归来屏蔽中断，防止在请求发出后，当前线程被中断后异常唤醒，导致在收到应答时无法正确处理
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

