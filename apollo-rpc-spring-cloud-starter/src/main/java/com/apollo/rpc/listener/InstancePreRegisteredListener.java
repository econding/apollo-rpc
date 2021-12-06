package com.apollo.rpc.listener;

import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 自动注册时
 * 向metedata添加rpc配置信息
 */
@Component
public class InstancePreRegisteredListener implements ApplicationListener<InstancePreRegisteredEvent>, EnvironmentAware {

    private Environment environment;

    @Override
    public void onApplicationEvent(InstancePreRegisteredEvent event) {
        String rpc_port = environment.getProperty("rpc.port");
        if(rpc_port != null && rpc_port.length() > 0){
            event.getRegistration().getMetadata().put("rpc_port",rpc_port);
        }
        String auth = environment.getProperty("rpc.auth");
        if(auth != null && auth.length() > 0){
            event.getRegistration().getMetadata().put("auth",auth);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
