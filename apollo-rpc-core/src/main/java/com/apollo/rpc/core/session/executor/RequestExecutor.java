package com.apollo.rpc.core.session.executor;


import com.apollo.rpc.core.msg.RPCReqBase;
import com.apollo.rpc.core.exception.RemoteServerDisabledException;
import com.apollo.rpc.core.exception.RPCException;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RequestExecutor<A extends RPCReqBase> {

    private static final Log log = LogFactory.getLog(RequestExecutor.class);
    public A reqBase;
    private volatile boolean weakUp;

    public Object doRequest(A reqBase, Channel channel) {

        this.reqBase = reqBase;

        if(channel != null){

            RequestMsgManager.putRequest(this);   //存储请求

            // 发送消息,此步骤与存储请求的顺序不能颠倒，否则在高并发情况下，存在一种情况：
            // 消息发出并且接受到了应答，但此时请求消息还未被放入缓存中，处理应答消息的线程无法找到被缓存的请求消息，造成信号丢失
            channel.writeAndFlush(reqBase);

            waiting();                            //等待

            if(reqBase.rspBase.responseCode != 0){
                throw RPCException.throwException(reqBase.rspBase); //异常处理
            }

        }else{
            throw new RemoteServerDisabledException(reqBase.serverName);
        }

        return reqBase.rspBase.responseParameter;

    }

    private synchronized void waiting(){
        while(!weakUp){
            try {
                wait();
            } catch (InterruptedException e) {
                log.error("The current thread cannot respond to an interrupt");
            }
        }
    }

    public synchronized void weakUp(){
        if(!weakUp){
            weakUp = true;
            notifyAll();
        }
    }

}

