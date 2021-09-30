package com.apollo.rpc.config;

import com.apollo.rpc.core.comm.RPCProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesResolve implements EnvironmentAware {

    private Environment environment;

    public RPCProperties getProperties(){

        RPCProperties properties = new RPCProperties();
        String rpc_port = environment.getProperty(RPCProperties.rpc_port);
        if(rpc_port == null){
            throw new NullPointerException("rpc_port must not be null");
        }
        properties.setProperty(RPCProperties.rpc_port,rpc_port);

        String server_name = environment.getProperty(RPCProperties.server_name);
        if(server_name == null){
            throw new NullPointerException("server_name must not be null");
        }
        properties.setProperty(RPCProperties.server_name,server_name);

        String auth = environment.getProperty(RPCProperties.auth);
        if(auth != null){
            properties.setProperty(RPCProperties.auth,auth);
        }

        String timeout = environment.getProperty(RPCProperties.timeout);
        if(timeout != null){
            properties.setProperty(RPCProperties.timeout,timeout);
        }

        String pool_size = environment.getProperty(RPCProperties.pool_size);
        if(pool_size != null){
            properties.setProperty(RPCProperties.pool_size,pool_size);
        }

        String permit_per_second = environment.getProperty(RPCProperties.permit_per_second);
        if(permit_per_second != null){
            properties.setProperty(RPCProperties.permit_per_second,permit_per_second);
        }

        String port = environment.getProperty(RPCProperties.port);
        if(port != null){
            properties.setProperty(RPCProperties.port,port);
        }

        return properties;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
