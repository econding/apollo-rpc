package com.apollo.rpc.remote;

import com.apollo.rpc.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.channel.Client;
import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.exception.RPCException;
import com.apollo.rpc.remote.instance.RemoteServerInstance;
import com.apollo.rpc.session.DefaultSessionFactory;
import com.apollo.rpc.session.RpcSession;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Channel认证管理
 */
public class ChannelHolder {

    private final static Log log = LogFactory.getLog(ChannelHolder.class);
    private Map<Channel, RemoteServerInstance> channelMap;
    private RemoteServerInfo remoteServerInfo = null;

    public ChannelHolder(){
        channelMap = new HashMap<>();
    }

    public Channel doConnect(String ip,String port){
        Channel channel =  new Client(ip,Integer.parseInt(port)).connect();
        if(channel != null){
            if(doAuth(channel)){
                return channel;
            }
            log.error("Authentication failed ip="+ip+" port="+port);
        }
        return null;
    }

    /**
     * 根据hashcode来决定本服务作为netty的客户端还是服务端
     * @param ip
     * @param port
     * @return
     */
    public boolean isClient(String ip,String port){
        if(remoteServerInfo.getIp().hashCode() > ip.hashCode()){
            return true;
        }else if(remoteServerInfo.getIp().hashCode() == ip.hashCode()){
            return remoteServerInfo.getPort().hashCode() > port.hashCode();
        }else{
            return false;
        }
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
        }catch  (RPCException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isAuth(Channel channel){
        return channelMap.containsKey(channel);
    }

    public void setRemoteServerInfo(RemoteServerInfo remoteServerInfo) {
        this.remoteServerInfo = remoteServerInfo;
    }

    public synchronized void addChannel(Channel channel,RemoteServerInstance instance){
        this.channelMap.put(channel,instance);
    }

    public synchronized void removeChannel(Channel channel){
        this.channelMap.remove(channel);
    }

}
