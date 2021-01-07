package com.apollo.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * 此注解用于指定接口所代理的远程服务
 * @return
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcClient {

    /**
     * 服务名称
     * @return
     */
    String value() default "";
}
