package com.apollo.rpc.factory;

import com.apollo.rpc.core.comm.RPCProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RPCProxyConfiguration implements ApplicationContextAware {


    private String packages;
    private ApplicationContext applicationContext;

    /**
     * 由于加载RpcProxyRegister时，spring容器未能注入任何属性，而要获取RpcProxyRegister，必须先去加载package配置信息
     */
    @Bean
    public RPCProxyRegister proxyRegister() {
        packages = applicationContext.getEnvironment().getProperty(RPCProperties.rpc_package);
        return new RPCProxyRegister(packages.split(","));
    }

    /**
     *  实现ApplicationContextAware接口，可以在加载RpcProxyRegister之前绑定ApplicationContext属性，通过ApplicationContext可以获取到配置信息
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
