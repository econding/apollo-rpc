package com.apollo.rpc.remote.server;

import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.msg.impl.RPCRequestMsg;
import com.apollo.rpc.remote.ChannelHolder;
import com.apollo.rpc.remote.RemoteServerHolder;
import com.apollo.rpc.remote.instance.RemoteServerInstance;
import com.apollo.rpc.remote.instance.RemoteServerInstanceImpl;
import com.apollo.rpc.exception.RPCException;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class RemoteServerImpl extends LoadBalanceFilter implements RemoteServer {

    private static final Log log = LogFactory.getLog(RemoteServerHolder.class);
    private ChannelHolder channelHolder;
    private String serverName;

    public RemoteServerImpl(String serverName){
        this.serverName = serverName;
    }

    @Override
    public String getServername() {
        return serverName;
    }

    @Override
    public Object doRequest(String ClassName, String Method, Object[] args) throws RPCException {
        RPCRequestMsg rpcRequestMsg = new RPCRequestMsg(Method,serverName,ClassName,args);
        return super.doRequest(rpcRequestMsg);
    }

    public void setChannelHolder(ChannelHolder channelHolder) {
        this.channelHolder = channelHolder;
    }

    public RemoteServerInstance getInstance(String ip, String port) {
        String key = ip+Constant.separator+port;
        for(RemoteServerInstance instance:super.instances){
            if(key.equals(instance.getIp()+Constant.separator+instance.getPort())){
                return instance;
            }
        }
        return null;
    }

    public void destroy() {
        for(RemoteServerInstance instance:super.instances){
            RemoteServerInstanceImpl instanceImpl = (RemoteServerInstanceImpl)instance;
            instanceImpl.destroy();
            channelHolder.removeChannel(instanceImpl.getChannel());
        }
        super.destroy();
    }

    public void active(RemoteServerInstance instance) {
        super.active(instance);
    }

    public void removeInstance(RemoteServerInstance instance){
        super.removeInstance(instance);
        log.info(instance.toString()+" has been de-registered");
    }

    public RemoteServerInstance newInstance(String ip, String port) {
        RemoteServerInstance remoteServerInstance = new RemoteServerInstanceImpl(ip,port,getServername());
        log.info(remoteServerInstance.toString()+" has been registered");
        super.addInstance(remoteServerInstance);
        return remoteServerInstance;
    }

    public void registerServerInstance(List<RemoteServerInfo> serverList){
        for(int i=instances.size()-1;i>=0;i--){
            RemoteServerInstanceImpl instance = (RemoteServerInstanceImpl)instances.get(i);
            boolean isExist = false;
            for(int j=serverList.size()-1;j>=0;j--){
                RemoteServerInfo serverInfo = serverList.get(j);
                if((instance.getIp()+ Constant.separator+instance.getPort()).equals(serverInfo.getIp()+Constant.separator+serverInfo.getPort())){
                    serverList.remove(j);
                    isExist = true;
                }
            }
            if(!isExist){//删除已过期的实例
                removeInstance(instance);
                channelHolder.removeChannel(instance.getChannel());
            }
        }
        //添加新的实例
        for(RemoteServerInfo serverInfo:serverList){
            RemoteServerInstanceImpl instance = (RemoteServerInstanceImpl)newInstance(serverInfo.getIp(),serverInfo.getPort());
            Channel channel = channelHolder.doConnect(instance.getIp(),instance.getPort());
            if(channel != null){
                instance.active(channel);
                active(instance);
            }
        }
    }

}
