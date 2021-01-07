package com.apollo.rpc.factory;

import com.apollo.rpc.annotation.RpcClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

class RPCProxyInterfacesScanner extends ClassPathBeanDefinitionScanner {

    public RPCProxyInterfacesScanner(BeanDefinitionRegistry registry) {
        //false表示不使用ClassPathBeanDefinitionScanner默认的TypeFilter
        super(registry, false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        this.addFilter();
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            return null;
        }
        this.createBeanDefinition(beanDefinitionHolders);
        return beanDefinitionHolders;
    }

    /**
     * 只扫描顶级接口
     * @param beanDefinition bean定义
     * @return boolean
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.getAnnotationTypes().contains(RpcClient.class.getName());
    }

    /**
     * 扫描所有类
     */
    private void addFilter() {
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }

    /**
     * 为扫描到的接口创建代理对象
     *
     * @param beanDefinitionHolders beanDefinitionHolders
     */
    private void createBeanDefinition(Set<BeanDefinitionHolder> beanDefinitionHolders) {
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition beanDefinition = ((GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition());
            //将bean的真实类型改变为FactoryBean
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(RPCProxyFactoryBean.class);
            beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

}
