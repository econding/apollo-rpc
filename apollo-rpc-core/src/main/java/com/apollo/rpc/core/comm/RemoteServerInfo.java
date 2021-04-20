package com.apollo.rpc.core.comm;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 当前服务实例的鉴权信息
 */
public class RemoteServerInfo {

    private String ip;

    private String name;
    /** 服务器主端口 */
    private String port;
    /** 服务器用于交换rpc报文的端口 */
    private String rpc_port;

    private String authMsg;

    public RemoteServerInfo(){

    }

    public RemoteServerInfo(String name, String port,String rpc_port, String authMsg) {
        this.authMsg = authMsg;
        this.name = name;
        this.port = port;
        this.rpc_port = rpc_port;
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    /** 获取服务器主端口 */
    public String getPort() {
        return port;
    }

    public String getAuthMsg() {
        return authMsg;
    }

    /** 获取服务器用于交换rpc报文的端口 */
    public String getRpcPort() {
        return rpc_port;
    }

    /** 设置服务器用于交换rpc报文的端口 */
    public void setRpcPort(String rpc_port) {
        this.rpc_port = rpc_port;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 设置服务器主端口 */
    public void setPort(String port) {
        this.port = port;
    }

    public void setAuthMsg(String authMsg) {
        this.authMsg = authMsg;
    }

}
