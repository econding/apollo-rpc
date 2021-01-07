package com.apollo.rpc.server.instance;


import com.apollo.rpc.msg.RPCReqBase;

/**
 * 远程服务实例代理
 */
public interface RemoteServerInstance {

    String getIp();

    String getPort() ;

    String getServerName();

    Object doRequest(RPCReqBase reqBase);

}
