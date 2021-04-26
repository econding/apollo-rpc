package com.apollo.rpc.config;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * RPC基础包扫描器
 */
public class RPCCoreBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public RPCCoreBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean b) {
        super(registry,b);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

}
