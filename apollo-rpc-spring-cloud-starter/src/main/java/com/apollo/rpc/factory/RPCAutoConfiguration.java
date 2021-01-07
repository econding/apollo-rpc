package com.apollo.rpc.factory;

import com.apollo.rpc.annotation.RpcService;
import com.apollo.rpc.comm.RPCProperties;
import com.apollo.rpc.config.PropertiesResolve;
import com.apollo.rpc.discovery.CloudServerDiscovery;
import com.apollo.rpc.service.RPCRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RPCAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PropertiesResolve propertiesResolve;
    @Autowired
    private CloudServerDiscovery cloudServerDiscovery;

    private RPCRegistry registry;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Map<String, Object> servers = event.getApplicationContext().getBeansWithAnnotation(RpcService.class);
        registry = registerServer(servers);
        registry.start();

    }

    private RPCRegistry registerServer(Map<String, Object> servers){
        RPCProperties properties = propertiesResolve.getProperties();
        RPCRegistry rpcRegistry = new RPCRegistry(properties,cloudServerDiscovery);
        for(Object server:servers.values()){
            rpcRegistry.register(server.getClass().getSimpleName(),server);
        }
        return rpcRegistry;
    }

}
