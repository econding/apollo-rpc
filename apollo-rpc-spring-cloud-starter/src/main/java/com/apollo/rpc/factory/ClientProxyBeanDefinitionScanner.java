package com.apollo.rpc.factory;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

public class ClientProxyBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public ClientProxyBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean b) {
        super(registry,b);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}
