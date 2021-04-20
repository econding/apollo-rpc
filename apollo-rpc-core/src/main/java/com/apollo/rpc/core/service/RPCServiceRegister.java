package com.apollo.rpc.core.service;

import java.util.HashMap;

public class RPCServiceRegister {

    private final HashMap<String, Object> services = new HashMap<>();

    public Object getService(String service){
        return services.get(service);
    }

    public void register(String service, Object object){
        services.put(service,object);
    }

}
