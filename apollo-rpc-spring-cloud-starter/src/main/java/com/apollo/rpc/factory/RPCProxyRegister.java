package com.apollo.rpc.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


class RPCProxyRegister implements BeanDefinitionRegistryPostProcessor {

    private String[] packages;

    public RPCProxyRegister(String[] packages){
        this.packages = packages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RPCProxyInterfacesScanner scanner = new RPCProxyInterfacesScanner(registry);
        scanner.doScan(packages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
