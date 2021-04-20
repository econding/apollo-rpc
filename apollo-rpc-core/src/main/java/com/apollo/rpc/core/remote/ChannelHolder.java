package com.apollo.rpc.core.remote;

import com.apollo.rpc.core.comm.Constant;
import com.apollo.rpc.core.comm.RemoteServerInfo;
import com.apollo.rpc.core.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.core.channel.Client;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.remote.instance.RemoteServerInstance;
import com.apollo.rpc.core.session.DefaultSessionFactory;
import com.apollo.rpc.core.session.RpcSession;
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

    public Channel doConnect(String ip,String rpc_port){
        return new Client(ip,Integer.parseInt(rpc_port)).connect();
    }

    /**
     * 根据hashcode来决定本服务作为netty的客户端还是服务端
     * @param ip ip
     * @param port 端口
     * @return 是否为客户端
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

    /**
     * 向远程服务发送验证信息
     * @param channel
     * @return
     */
    public boolean doAuth(Channel channel){

        RPCAuthReqMsg rpcAuthReqMsg = new RPCAuthReqMsg();
        rpcAuthReqMsg.msgType = Constant.server_auth;
        rpcAuthReqMsg.serverName = remoteServerInfo.getName();
        rpcAuthReqMsg.authMsg = remoteServerInfo.getAuthMsg();
        rpcAuthReqMsg.authServerName = remoteServerInfo.getName();
        rpcAuthReqMsg.authIp = remoteServerInfo.getIp();
        rpcAuthReqMsg.authPort = remoteServerInfo.getPort();

        RpcSession serverAuthSession = DefaultSessionFactory.instance.createSession(channel);
        try {
            serverAuthSession.request(rpcAuthReqMsg);
        }catch  (RPCException e) {
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
        channelMap.put(channel,instance);
    }

    public synchronized void removeChannel(Channel channel){
        channelMap.remove(channel);
    }

}
