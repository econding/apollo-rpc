package com.apollo.rpc.core.remote.server;

import com.apollo.rpc.core.comm.Constant;
import com.apollo.rpc.core.comm.RemoteServerInfo;
import com.apollo.rpc.core.remote.ChannelHolder;
import com.apollo.rpc.core.remote.RemoteServerContainer;
import com.apollo.rpc.core.remote.instance.RemoteServerInstance;
import com.apollo.rpc.core.remote.instance.RemoteServerInstanceImpl;
import com.apollo.rpc.core.task.RPCTaskScheduler;
import com.apollo.rpc.core.msg.impl.RPCRequestMsg;
import com.apollo.rpc.core.exception.RPCException;
import com.apollo.rpc.core.task.RPCScheduledRunnable;
import io.netty.channel.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class RemoteServerImpl extends LoadBalanceFilter implements RemoteServer {

    private static final Log log = LogFactory.getLog(RemoteServerImpl.class);
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
    public Object invoke(String ClassName, String Method, Object[] args) {
        RPCRequestMsg rpcRequestMsg = new RPCRequestMsg(Method,serverName,ClassName,args);
        return super.invoke(rpcRequestMsg);
    }

    public void setChannelHolder(ChannelHolder channelHolder) {
        this.channelHolder = channelHolder;
    }

    public RemoteServerInstance getInstance(String ip, String port) {
        String key = ip+ Constant.separator+port;
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
        RemoteServerInstanceImpl remoteServerInstanceImpl = (RemoteServerInstanceImpl)instance;
        remoteServerInstanceImpl.destroy();
        channelHolder.removeChannel(remoteServerInstanceImpl.getChannel());
        super.removeInstance(remoteServerInstanceImpl);
        log.info(remoteServerInstanceImpl.toString()+" has been de-registered");
    }

    public RemoteServerInstance newInstance(String ip, String port, String rpc_port) {
        RemoteServerInstance remoteServerInstance = new RemoteServerInstanceImpl(ip,port,rpc_port,getServername());
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
            if(!isExist){   //删除已过期的实例
                removeInstance(instance);
            }else{
                if(instance.getChannel() != null && !instance.getChannel().isActive()){   //如果Channel被关闭，则直接删除对应的实例
                    instance.inActive();
                }
            }
        }
        //添加新的实例
        for(RemoteServerInfo serverInfo:serverList){
            RemoteServerInstanceImpl instance = (RemoteServerInstanceImpl)newInstance(serverInfo.getIp(),serverInfo.getPort(),serverInfo.getRpcPort());
            if(channelHolder.isClient(instance.getIp(),instance.getPort())){              //客戶端需要主动发起连接
                RPCTaskScheduler.schedule(new ChannelConnectTask(instance),500);
            }
        }
    }

    /**
     * Channel连接异步任务
     */
    private class ChannelConnectTask extends RPCScheduledRunnable {

        private RemoteServerInstanceImpl instance;
        private Channel channel = null;

        public ChannelConnectTask(RemoteServerInstanceImpl instance){
            this.instance = instance;
        }

        @Override
        public void run() {

            if(instances.contains(instance)){             //服务实例仍存在
                if(channel == null){                      //channel为空，则建立连接
                    channel = channelHolder.doConnect(instance.getIp(),instance.getRpcPort());
                }
                if(channel != null){                      //channel不为空，则发起鉴权操作
                    if(channelHolder.doAuth(channel,instance)){    //鉴权成功，则激活服务实例，并取消此task任务
                        instance.active(channel);
                        active(instance);
                        channelHolder.addChannel(channel,instance);
                        cancel();
                    }
                }
            }else{
                if(channel != null){                     //不存在服务实例，但是channel存在，则断开连接，并取消此task
                    if(channel.isOpen() || channel.isActive()){
                        channel.close();
                    }
                }
                cancel();
            }

        }
    }


}
