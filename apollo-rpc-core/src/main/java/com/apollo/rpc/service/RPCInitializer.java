package com.apollo.rpc.service;

import com.apollo.rpc.channel.Client;
import com.apollo.rpc.client.proxy.RPCProxy;
import com.apollo.rpc.comm.RPCProperties;
import com.apollo.rpc.comm.RemoteServerInfo;
import com.apollo.rpc.concurrent.RPCExecutorService;
import com.apollo.rpc.handler.*;
import com.apollo.rpc.channel.RPCChannelInboundHandler;
import com.apollo.rpc.channel.RPCChannelInitializer;
import com.apollo.rpc.channel.Server;
import com.apollo.rpc.remote.RemoteServerDiscovery;
import com.apollo.rpc.remote.RemoteServerHolder;
import com.apollo.rpc.session.executor.RequestMsgManager;

public class RPCInitializer {

    private static final int default_timeout = 20 * 1000;

    private RPCProperties properties;
    private RPCDispatchService dispatchService;
    private RPCServiceRegister serviceRegister;
    private RemoteServerHolder remoteServerHolder;
    private AuthenticationService authenticationService;
    private RPCChannelInitializer channelInitializer;
    private RPCExecutorService rpcExecutorService;

    private RemoteServerDiscovery discovery;

    public RPCInitializer(RPCProperties properties, RemoteServerDiscovery discovery){
        this.properties = properties;
        this.discovery = discovery;
        initService();
        initHandler();
        initChannelInitializer();
        initClient();
        initSession();
        remoteServerHolder.setRemoteServerInfo(getServerInfo());
        authenticationService.setRemoteServerHolder(remoteServerHolder);

    }

    public void initSession(){
        RequestMsgManager.initialize(properties.getInt(RPCProperties.timeout,default_timeout));
    }

    public void initService(){

        dispatchService       = new RPCDispatchService();
        serviceRegister       = new RPCServiceRegister();
        remoteServerHolder    = new RemoteServerHolder();
        authenticationService = new AuthenticationService();
        rpcExecutorService    = new RPCExecutorService();

        rpcExecutorService.init(properties);
        remoteServerHolder.setDiscovery(discovery);
        dispatchService.setAuthenticationService(authenticationService);
    }

    public void initHandler(){

        RPCAuthReqMsgHandler authReqMsgHandler = new RPCAuthReqMsgHandler();
        authReqMsgHandler.initHandler(authenticationService);
        dispatchService.registerHandler(authReqMsgHandler);

        RPCAuthRspMsgHandler authRspMsgHandler = new RPCAuthRspMsgHandler();
        dispatchService.registerHandler(authRspMsgHandler);

        RPCRequestMsgHandler requestMsgHandler = new RPCRequestMsgHandler();
        requestMsgHandler.initHandler(rpcExecutorService,serviceRegister);
        dispatchService.registerHandler(requestMsgHandler);

        RPCResponseMsgHandler responseMsgHandler = new RPCResponseMsgHandler();
        dispatchService.registerHandler(responseMsgHandler);

        RPCServerCheckReqMsgHandler rpcServerCheckReqMsgHandler = new RPCServerCheckReqMsgHandler();
        rpcServerCheckReqMsgHandler.initHandler(rpcExecutorService);
        dispatchService.registerHandler(rpcServerCheckReqMsgHandler);

        RPCServerCheckRspMsgHandler rpcServerCheckRspMsgHandler = new RPCServerCheckRspMsgHandler();
        dispatchService.registerHandler(rpcServerCheckRspMsgHandler);

    }

    public void initClient(){
        Client.setChannelInitializer(channelInitializer);
        RPCProxy.setRemoteServerHolder(remoteServerHolder);
    }

    public void initChannelInitializer(){
        channelInitializer = new RPCChannelInitializer();
        RPCChannelInboundHandler.setDispatchService(dispatchService);
    }

    public RemoteServerInfo getServerInfo(){

        String port = properties.getString(RPCProperties.port);
        String auth = properties.getString(RPCProperties.auth);
        String serverName = properties.getString(RPCProperties.server_name);
        RemoteServerInfo serverInfo = new RemoteServerInfo(serverName,port,auth);
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
        String port = properties.getString(RPCProperties.port);
        if(port == null || port.length() == 0){
            throw new NullPointerException("properties:rpc-port must not be null");
        }
        rpc_port = new Integer(port);
        Server server = new Server();
        server.setChannelInitializer(channelInitializer);
        server.start(rpc_port);

    }

}
