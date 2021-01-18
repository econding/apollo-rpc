package com.apollo.rpc.discovery;

import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.remote.RemoteServerDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CloudServerDiscovery implements RemoteServerDiscovery {

    public static final String rpc_port = "rpc-port";
    public static final String auth = "auth";

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String serverName;

    @Override
    public Map<String, List<RemoteServerInfo>> getServerInfo() {
        Map<String, List<RemoteServerInfo>> serverMap = new HashMap<>();

        List<String> servers = discoveryClient.getServices();
        if(servers != null){
            for(String serverID:servers){
                if(serverID.equals(serverName)){
                    continue;
                }
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serverID);
                if(serviceInstances.size() == 0){
                    continue;
                }
                List<RemoteServerInfo> serverInfos = new ArrayList<>();
                for(ServiceInstance serviceInstance:serviceInstances){
                    RemoteServerInfo serverInfo = new RemoteServerInfo();
                    serverInfo.setIp(serviceInstance.getHost());
                    serverInfo.setAuthMsg(serviceInstance.getMetadata().get(auth));
                    serverInfo.setPort(serviceInstance.getMetadata().get(rpc_port));
                    serverInfo.setName(serverID);
                    serverInfos.add(serverInfo);
                }
                serverMap.put(serverID,serverInfos);
            }
        }
        return serverMap;
    }

}
