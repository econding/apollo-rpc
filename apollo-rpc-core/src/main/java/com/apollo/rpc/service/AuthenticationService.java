package com.apollo.rpc.service;

import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.remote.RemoteServerHolder;
import com.apollo.rpc.remote.instance.RemoteServerInstanceImpl;
import com.apollo.rpc.remote.server.RemoteServerImpl;
import io.netty.channel.Channel;

public class AuthenticationService {

    private RemoteServerHolder remoteServerHolder;

    public void setRemoteServerHolder(RemoteServerHolder remoteServerHolder) {
        this.remoteServerHolder = remoteServerHolder;
    }

    /**
     * 处理接收到的连接请求
     * @param channel
     * @param rpcAuthReqMsg
     * @return
     */
    public boolean authenticate(Channel channel, RPCAuthReqMsg rpcAuthReqMsg){
        RemoteServerImpl remoteServer = (RemoteServerImpl)remoteServerHolder.getRemoteServer(rpcAuthReqMsg.authServerName);
        if(remoteServer != null){
            RemoteServerInstanceImpl instance = (RemoteServerInstanceImpl)remoteServer.getInstance(rpcAuthReqMsg.authIp,rpcAuthReqMsg.authPort);
            if(instance != null){
                if(doAuth(rpcAuthReqMsg)){
                    instance.active(channel);
                    remoteServer.active(instance);
                    remoteServerHolder.getChannelHolder().addChannel(channel,instance);
                    return true;
                }
            }else{
                remoteServerHolder.getServerInfo();
                return authenticate(channel,rpcAuthReqMsg);
            }
        }else{
            remoteServerHolder.getServerInfo();
            return authenticate(channel,rpcAuthReqMsg);
        }
        return false;
    }

    /**
     * 授权
     * @param rpcAuthReqMsg
     * @return
     */
    private boolean doAuth(RPCAuthReqMsg rpcAuthReqMsg){
        String key = rpcAuthReqMsg.authIp+ Constant.separator+rpcAuthReqMsg.authPort;
        RemoteServerInfo authInfo = remoteServerHolder.getRemoteServerInfo(key);
        if(authInfo != null){
            if(authInfo.getAuthMsg().equals(rpcAuthReqMsg.authMsg)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断服务是否经过授权
     * @param channel
     * @return
     */
    public boolean isAuth(Channel channel){
        return remoteServerHolder.getChannelHolder().isAuth(channel);
    }

}
