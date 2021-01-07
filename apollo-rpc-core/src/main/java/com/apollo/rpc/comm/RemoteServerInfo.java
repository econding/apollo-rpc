package com.apollo.rpc.comm;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 当前服务实例的鉴权信息
 */
public class RemoteServerInfo {

    private String ip;

    private String name;

    private String port;

    private String authMsg;

    public RemoteServerInfo(){

    }

    public RemoteServerInfo(String name, String port, String authMsg) {
        this.authMsg = authMsg;
        this.name = name;
        this.port = port;
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

    public String getPort() {
        return port;
    }

    public String getAuthMsg() {
        return authMsg;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setAuthMsg(String authMsg) {
        this.authMsg = authMsg;
    }

}
