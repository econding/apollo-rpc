package com.apollo.rpc.core.service;

import com.apollo.rpc.core.comm.Constant;
import com.apollo.rpc.core.comm.RemoteServerInfo;
import com.apollo.rpc.core.msg.impl.RPCAuthReqMsg;
import com.apollo.rpc.core.remote.RemoteServerContainer;
import com.apollo.rpc.core.remote.instance.RemoteServerInstanceImpl;
import com.apollo.rpc.core.remote.server.RemoteServerImpl;
import io.netty.channel.Channel;

public class AuthenticationService {

    private RemoteServerContainer remoteServerContainer;

    public void setRemoteServerContainer(RemoteServerContainer remoteServerContainer) {
        this.remoteServerContainer = remoteServerContainer;
    }

    /**
     * 处理接收到的连接请求
     * @param channel
     * @param rpcAuthReqMsg
     * @return
     */
    public boolean authenticate(Channel channel, RPCAuthReqMsg rpcAuthReqMsg){
        RemoteServerImpl remoteServer = (RemoteServerImpl) remoteServerContainer.getRemoteServer(rpcAuthReqMsg.authServerName);
        if(remoteServer != null){
            RemoteServerInstanceImpl instance = (RemoteServerInstanceImpl)remoteServer.getInstance(rpcAuthReqMsg.authIp,rpcAuthReqMsg.authPort);
            if(instance != null){
                if(doAuth(rpcAuthReqMsg)){
                    instance.active(channel);
                    remoteServer.active(instance);
                    remoteServerContainer.getChannelHolder().addChannel(channel,instance);
                    return true;
                }
            }else{
                return false;
            }
        }else{
            return false;
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
        RemoteServerInfo authInfo = remoteServerContainer.getRemoteServerInfo(key);
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
        return remoteServerContainer.getChannelHolder().isAuth(channel);
    }

}
