package com.apollo.rpc.core.comm;

import java.util.HashMap;
import java.util.Map;

public class RPCProperties {

    public static final String rpc_port = "rpc.port";
    public static final String port = "server.port";
    public static final String auth = "rpc.auth";
    public static final String rpc_package = "rpc.client.package";
    public static final String timeout = "rpc.client.timeout";
    public static final String pool_size = "rpc.server.pool_size";
    public static final String permit_per_second = "rpc.server.permit_per_second";
    public static final String max_permits = "rpc.server.max_permits";
    public static final String server_name = "spring.application.name";

    private Map<String,Object> properties;

    public RPCProperties(){
        properties = new HashMap<>();
    }

    public void setProperty(String key,Object value) {
        properties.put(key,value);
    }

    public String getString(String key){
        return (String)properties.get(key);
    }

    public int getInt(String key,int defaultValue){
        String value = getString(key);
        if(value == null || value.length() == 0){
            return defaultValue;
        }
        return new Integer(value);
    }

}
