package com.apollo.rpc.factory;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启动时扫描RPC默认的包
 */
public class RPCImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    /**需要扫描包路径*/
    public static final String package_properties = "com.apollo.rpc";

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //扫描类
        ClientProxyBeanDefinitionScanner scanner =
                new ClientProxyBeanDefinitionScanner(registry, true);//使用默认的过滤器
        scanner.setResourceLoader(resourceLoader);
        scanner.scan(package_properties);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

}
