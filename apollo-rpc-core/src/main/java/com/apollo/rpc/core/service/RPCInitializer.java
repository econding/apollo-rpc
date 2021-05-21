package com.apollo.rpc.core.service;

import com.apollo.rpc.core.channel.Client;
import com.apollo.rpc.core.handler.*;
import com.apollo.rpc.core.proxy.RPCProxy;
import com.apollo.rpc.core.comm.RPCProperties;
import com.apollo.rpc.core.comm.RemoteServerInfo;
import com.apollo.rpc.core.concurrent.RPCExecutorService;
import com.apollo.rpc.core.channel.RPCChannelInboundHandler;
import com.apollo.rpc.core.channel.RPCChannelInitializer;
import com.apollo.rpc.core.channel.Server;
import com.apollo.rpc.core.remote.RemoteServerDiscovery;
import com.apollo.rpc.core.remote.RemoteServerContainer;
import com.apollo.rpc.core.session.executor.RequestMsgManager;

/**
 * RPC服务初始化类
 */
public class RPCInitializer {

    private static final int default_timeout = 20 * 1000;

    private RPCProperties         properties;
    private RPCDispatchService    dispatchService;
    private RPCServiceRegister    serviceRegister;
    private RemoteServerContainer remoteServerContainer;
    private AuthenticationService authenticationService;
    private RPCChannelInitializer channelInitializer;
    private RPCExecutorService    rpcExecutorService;

    private RemoteServerDiscovery discovery;

    public RPCInitializer(RPCProperties properties, RemoteServerDiscovery discovery){
        this.properties = properties;
        this.discovery = discovery;
        initService();
        initHandler();
        initChannelInitializer();
        initClient();
        initSession();
        remoteServerContainer.setRemoteServerInfo(getServerInfo());
        authenticationService.setRemoteServerContainer(remoteServerContainer);

    }

    /**
     * 设置报文应答的超时时间
     */
    public void initSession(){
        RequestMsgManager.initialize(properties.getInt(RPCProperties.timeout,default_timeout));
    }

    /**
     * 初始化各类服务
     */
    public void initService(){

        dispatchService       = new RPCDispatchService();
        serviceRegister       = new RPCServiceRegister();
        remoteServerContainer = new RemoteServerContainer();
        authenticationService = new AuthenticationService();
        rpcExecutorService    = new RPCExecutorService();

        rpcExecutorService.init(properties);
        remoteServerContainer.setDiscovery(discovery);
        dispatchService.setAuthenticationService(authenticationService);
    }

    /**
     * 注册报文handler
     */
    public void initHandler(){

        RPCAuthReqMsgHandler authReqMsgHandler                  = new RPCAuthReqMsgHandler();
        authReqMsgHandler.initHandler(authenticationService);
        dispatchService.registerHandler(authReqMsgHandler);

        RPCAuthRspMsgHandler authRspMsgHandler                  = new RPCAuthRspMsgHandler();
        dispatchService.registerHandler(authRspMsgHandler);

        RPCRequestMsgHandler requestMsgHandler                  = new RPCRequestMsgHandler();
        requestMsgHandler.initHandler(rpcExecutorService,serviceRegister);
        dispatchService.registerHandler(requestMsgHandler);

        RPCResponseMsgHandler responseMsgHandler                = new RPCResponseMsgHandler();
        dispatchService.registerHandler(responseMsgHandler);

        RPCServerCheckReqMsgHandler rpcServerCheckReqMsgHandler = new RPCServerCheckReqMsgHandler();
        rpcServerCheckReqMsgHandler.initHandler(rpcExecutorService);
        dispatchService.registerHandler(rpcServerCheckReqMsgHandler);

        RPCServerCheckRspMsgHandler rpcServerCheckRspMsgHandler = new RPCServerCheckRspMsgHandler();
        dispatchService.registerHandler(rpcServerCheckRspMsgHandler);

    }

    public void initClient(){
        Client.setChannelInitializer(channelInitializer);
        RPCProxy.setRemoteServerHolder(remoteServerContainer);
    }

    public void initChannelInitializer(){
        channelInitializer = new RPCChannelInitializer();
        RPCChannelInboundHandler.setDispatchService(dispatchService);
    }

    public RemoteServerInfo getServerInfo(){
        String rpc_port             = properties.getString(RPCProperties.rpc_port);
        String port                 = properties.getString(RPCProperties.port);
        String auth                 = properties.getString(RPCProperties.auth);
        String serverName           = properties.getString(RPCProperties.server_name);
        RemoteServerInfo serverInfo = new RemoteServerInfo(serverName,port,rpc_port,auth);
        return serverInfo;
    }

    public void register(String serviceName,Object service){
        serviceRegister.register(serviceName,service);
    }

    public void start()   {

        if(this.properties == null){
            throw new NullPointerException("properties must not be null");
        }
        if(this.properties == null){
            throw new NullPointerException("RemoteServerDiscovery must not be null");
        }
        int rpc_port;
        String port = properties.getString(RPCProperties.rpc_port);
        if(port == null || port.length() == 0){
            throw new NullPointerException("properties:rpc-port must not be null");
        }
        rpc_port = new Integer(port);
        Server server = new Server();
        server.setChannelInitializer(channelInitializer);
        server.start(rpc_port);

    }

}
