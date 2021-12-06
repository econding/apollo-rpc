package com.apollo.rpc.config;

import com.apollo.rpc.annotation.RpcService;
import com.apollo.rpc.core.comm.RPCProperties;
import com.apollo.rpc.discovery.CloudServerDiscovery;
import com.apollo.rpc.core.service.RPCInitializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RPCAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Log log = LogFactory.getLog(RPCAutoConfiguration.class);

    @Autowired
    private PropertiesResolve propertiesResolve;
    @Autowired
    private CloudServerDiscovery cloudServerDiscovery;

    private RPCInitializer registry;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        registry = getRPCInitializer(event.getApplicationContext());
        registry.start();
        log.info("RPC Service Started on port:"+propertiesResolve.getProperties().getString(RPCProperties.rpc_port));
    }

    private RPCInitializer getRPCInitializer(ApplicationContext context){
        Map<String, Object> servers = context.getBeansWithAnnotation(RpcService.class);
        RPCProperties properties = propertiesResolve.getProperties();
        RPCInitializer rpcInitializer = new RPCInitializer(properties,cloudServerDiscovery);
        for(Object service:servers.values()){
            rpcInitializer.register(service.getClass().getSimpleName(),service);
        }
        return rpcInitializer;
    }

}
