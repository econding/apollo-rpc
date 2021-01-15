package com.apollo.rpc.remote;

import com.apollo.rpc.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.channel.Client;
import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.session.DefaultSessionFactory;
import com.apollo.rpc.session.RpcSession;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Channel认证管理
 */
public class ChannelHolder {

    private final static Log log = LogFactory.getLog(ChannelHolder.class);
    private List<Channel> channelList;
    private RemoteServerInfo remoteServerInfo = null;

    public ChannelHolder(){
        channelList = new ArrayList<>();
    }

    public Channel doConnect(String ip,String port){
        if((remoteServerInfo.getIp()+Constant.separator+remoteServerInfo.getPort()).hashCode() < (ip+Constant.separator+port).hashCode()){
            Channel channel =  new Client(ip,Integer.parseInt(port)).connect();
            if(doAuth(channel)){
                addChannel(channel);
                return channel;
            }
        }
        log.error("Authentication failed ip="+ip+" port="+port);
        return null;
    }

    private boolean doAuth(Channel channel){

        RPCAuthReqMsg rpcAuthReqMsg = new RPCAuthReqMsg();
        rpcAuthReqMsg.msgType = Constant.server_auth;
        rpcAuthReqMsg.serverName = remoteServerInfo.getName();
        rpcAuthReqMsg.authMsg = remoteServerInfo.getAuthMsg();
        rpcAuthReqMsg.authServerName = remoteServerInfo.getName();
        rpcAuthReqMsg.authIp = remoteServerInfo.getIp();
        rpcAuthReqMsg.authPort = remoteServerInfo.getPort();

        RpcSession serverAuthSession = DefaultSessionFactory.instance.createSession(channel);
        try {
            serverAuthSession.doRequest(rpcAuthReqMsg);
        } catch  (RPCException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isAuth(Channel channel){
        return channelList.contains(channel);
    }

    public void setRemoteServerInfo(RemoteServerInfo remoteServerInfo) {
        this.remoteServerInfo = remoteServerInfo;
    }

    public synchronized void addChannel(Channel channel){
        this.channelList.add(channel);
    }

    public synchronized void removeChannel(Channel channel){
        this.channelList.remove(channel);
    }

}
