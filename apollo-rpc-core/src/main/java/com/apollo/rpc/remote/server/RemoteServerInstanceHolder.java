package com.apollo.rpc.remote.server;

import com.apollo.rpc.remote.instance.RemoteServerInstance;

import java.util.ArrayList;
import java.util.List;

public class RemoteServerInstanceHolder {

    protected List<RemoteServerInstance> instances = null;

    public RemoteServerInstanceHolder(){
        instances = new ArrayList<>();
    }

    protected void addInstance(RemoteServerInstance instance){
        instances.add(instances.size(),instance);
    }

    protected int getIndex(RemoteServerInstance instance){
        return instances.indexOf(instance);
    }

    protected RemoteServerInstance getInstance(int index){
        return instances.get(index);
    }

    protected void removeInstance(int index){
        instances.remove(index);
    }

    protected void removeInstance(RemoteServerInstance instance){
        instances.remove(instance);
    }

}
