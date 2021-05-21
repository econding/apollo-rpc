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
    private boolean weakUp;

    public Object doRequest(A reqBase, Channel channel) {

        this.reqBase = reqBase;

        if(channel != null){

            RequestMsgManager.putRequest(this);   //存储请求

            channel.writeAndFlush(reqBase);       //发送消息

            waiting();                     //等待

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

