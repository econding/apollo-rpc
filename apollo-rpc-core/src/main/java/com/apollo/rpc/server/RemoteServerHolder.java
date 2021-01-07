package com.apollo.rpc.server;

import com.apollo.rpc.comm.CommonUtil;
import com.apollo.rpc.server.server.RemoteServer;
import com.apollo.rpc.server.server.RemoteServerImpl;

import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.comm.RemoteServerInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteServerHolder {

    private static final Log log = LogFactory.getLog(RemoteServerHolder.class);
    private static final int Server_Update_Time = 1000;

    private ChannelHolder channelHolder = null;
    private Map<String,RemoteServer> servers = null;
    private RemoteServerInfo remoteServerInfo = null;
    private Map<String, RemoteServerInfo> rpcAuthInfoMap = null;
    private RemoteServerDiscovery discovery = null;

    public RemoteServerHolder(){

        channelHolder = new ChannelHolder();
        servers = new HashMap<>();
        rpcAuthInfoMap = new HashMap<>();

    }

    public void setRemoteServerInfo(RemoteServerInfo remoteServerInfo) {
        this.remoteServerInfo = remoteServerInfo;
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
        RPCTaskRunner.execute(serverUpdater);
    }

    public synchronized void getServerInfo(){
        Map<String,List<RemoteServerInfo>> servers = discovery.getServerInfo();
        updateServerList(servers);
    }

    private void updateServerList(Map<String,List<RemoteServerInfo>> serverMap){
        this.rpcAuthInfoMap.clear();
        for(String serverName:servers.keySet()){
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
            log.info("server: "+ serverName+" has been registered");
            return remoteServer;
        }
        return servers.get(serverName);
    }

    private void removeRemoteServer(String serverName){
        RemoteServerImpl remoteServer = (RemoteServerImpl) servers.get(serverName);
        remoteServer.destroy();
        servers.remove(serverName);
        log.info("server: "+ serverName+" has been de-registered");
    }

    private class ServerUpdater implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    getServerInfo();
                    CommonUtil.sleep(Server_Update_Time);
                }catch (Exception e){
                    log.error("unchecked exception occurred while updating",e);
                }
            }
        }
    }
}
