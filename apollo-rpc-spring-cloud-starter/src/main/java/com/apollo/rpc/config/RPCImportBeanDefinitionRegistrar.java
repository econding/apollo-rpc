package com.apollo.rpc.config;

import com.apollo.rpc.annotation.RpcClient;
import com.apollo.rpc.factory.RPCProxyFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * RPC核心组件注册器
 */
public class RPCImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    /**需要扫描包路径*/
    public static final String package_properties = "com.apollo.rpc.core";

    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //先注册 "com.apollo.rpc.core" 下所有的组件
        RPCCoreBeanDefinitionScanner coreScanner =
                new RPCCoreBeanDefinitionScanner(registry, true);//使用默认的过滤器
        coreScanner.setResourceLoader(resourceLoader);
        coreScanner.scan(package_properties);
        //第二步 扫描应用下所有包含 @RpcClient 注解的bean，并为此类注解创建FactoryBean
        Set<String> basePackages = getBasePackages(importingClassMetadata);
        //获取扫描器
        ClassPathScanningCandidateComponentProvider rpcClientProxyBeanScanner = getScanner();
        //扫描并注册bd
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = rpcClientProxyBeanScanner.findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidateComponents) {
                if(GenericBeanDefinition.class.isAssignableFrom(beanDefinition.getClass())) {
                    ScannedGenericBeanDefinition genericBeanDefinition = ((ScannedGenericBeanDefinition) beanDefinition);
                    String beanName = genericBeanDefinition.getBeanClassName();
                    //将bean的真实类型改变为FactoryBean
                    genericBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(genericBeanDefinition.getBeanClassName());
                    genericBeanDefinition.setBeanClass(RPCProxyFactoryBean.class);
                    genericBeanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    //注册bd
                    registry.registerBeanDefinition(beanName, genericBeanDefinition);
                }
            }
        }
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        //不使用默认的过滤器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, environment){
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                AnnotationMetadata metadata = beanDefinition.getMetadata();
                return metadata.getAnnotationTypes().contains(RpcClient.class.getName());
            }
        };
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        return scanner;
    }

    /**
     * 获取扫描组件的包路径
     */
    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(SpringBootApplication.class.getCanonicalName());  //获取SpringBootApplication上定义的所有需要扫描的包路径
        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("scanBasePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class<?>[]) attributes.get("scanBasePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        //默认的包路径
        basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
