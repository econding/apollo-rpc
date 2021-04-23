package com.apollo.rpc.core.remote;

import com.apollo.rpc.core.session.executor.RequestMsgManager;
import com.apollo.rpc.core.task.RPCTaskScheduler;
import com.apollo.rpc.core.remote.server.RemoteServer;
import com.apollo.rpc.core.remote.server.RemoteServerImpl;

import com.apollo.rpc.core.comm.Constant;
import com.apollo.rpc.core.comm.RemoteServerInfo;
import com.apollo.rpc.core.task.RPCScheduledRunnable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class RemoteServerContainer {

    private static final Log log = LogFactory.getLog(RemoteServerContainer.class);
    private static final long Server_Update_Time = 1000;

    private ChannelHolder channelHolder;
    private Map<String,RemoteServer> servers;
    private Map<String, RemoteServerInfo> rpcAuthInfoMap;
    private RemoteServerDiscovery discovery = null;

    public RemoteServerContainer(){
        channelHolder = new ChannelHolder();
        servers = new HashMap<>();
        rpcAuthInfoMap = new HashMap<>();
    }

    public void setRemoteServerInfo(RemoteServerInfo remoteServerInfo) {
        channelHolder.setRemoteServerInfo(remoteServerInfo);
    }

    public ChannelHolder getChannelHolder() {
        return channelHolder;
    }

    public RemoteServer getRemoteServer(String serverName){
        return servers.get(serverName);
    }

    public RemoteServerInfo getRemoteServerInfo(String serverName){
        return rpcAuthInfoMap.get(serverName);
    }

    public void setDiscovery(RemoteServerDiscovery discovery) {
        this.discovery = discovery;
        ServerUpdater serverUpdater = new ServerUpdater();
        RPCTaskScheduler.schedule(serverUpdater,Server_Update_Time);
    }

    public synchronized void getServerInfo(){
        Map<String,List<RemoteServerInfo>> servers = discovery.getServices();
        updateServerList(servers);
    }

    private void updateServerList(Map<String,List<RemoteServerInfo>> serverMap){
        this.rpcAuthInfoMap.clear();
        List<String> list = new ArrayList<>(servers.keySet());
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String serverName = iterator.next();
            if(serverMap.containsKey(serverName)){
                updateServerInstanceList(serverName,serverMap.get(serverName));
            }else{
                removeRemoteServer(serverName);
            }
        }
        for(String serverName : serverMap.keySet()){
            if(!servers.containsKey(serverName)){
                newServer(serverName);
                updateServerInstanceList(serverName,serverMap.get(serverName));
            }
        }
    }

    private void updateServerInstanceList(String serverName,List<RemoteServerInfo> list){
        RemoteServerImpl remoteServer = (RemoteServerImpl)servers.get(serverName);
        if(remoteServer == null){
            remoteServer = (RemoteServerImpl)newServer(serverName);
        }
        for(RemoteServerInfo remoteServerInfo:list){
            String key = remoteServerInfo.getIp()+Constant.separator+remoteServerInfo.getPort();
            rpcAuthInfoMap.put(key,remoteServerInfo);
        }
        remoteServer.registerServerInstance(list);
    }

    private RemoteServer newServer(String serverName){
        if(!servers.containsKey(serverName)){
            RemoteServerImpl remoteServer = new RemoteServerImpl(serverName);
            servers.put(serverName,remoteServer);
            remoteServer.setChannelHolder(channelHolder);
            RequestMsgManager.createCacheMapForNewServer(serverName);
            log.info("server: "+ serverName+" has been registered");
            return remoteServer;
        }
        return servers.get(serverName);
    }

    private void removeRemoteServer(String serverName){
        RemoteServerImpl remoteServer = (RemoteServerImpl) servers.get(serverName);
        remoteServer.destroy();
        servers.remove(serverName);
        RequestMsgManager.removeCacheMapForServer(serverName);
        log.info("server: "+ serverName+" has been de-registered");
    }

    private class ServerUpdater extends RPCScheduledRunnable {
        @Override
        public void run() {
            try {
                getServerInfo();
            }catch (Exception e){
                log.error("unchecked exception occurred while updating",e);
            }
        }
    }
}
