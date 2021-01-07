package com.apollo.rpc.annotation;

import com.apollo.rpc.factory.RPCImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RPCImportBeanDefinitionRegistrar.class)
public @interface EnableRpcService {

}
