package com.apollo.rpc.core.remote.instance;


import com.apollo.rpc.core.msg.RPCReqBase;

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
