package com.apollo.rpc.remote.instance;


import com.apollo.rpc.msg.RPCReqBase;

/**
 * 远程服务实例代理
 */
public interface RemoteServerInstance {

    String getIp();

    String getPort() ;

    String getServerName();

    Object invoke(RPCReqBase reqBase);

    boolean isActive();

    void inActive();
}
