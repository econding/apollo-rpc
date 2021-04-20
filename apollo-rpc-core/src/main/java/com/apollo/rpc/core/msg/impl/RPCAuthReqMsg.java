package com.apollo.rpc.core.msg.impl;

import com.apollo.rpc.core.msg.RPCReqBase;

/**
 * 客户端认证请求消息
 */
public class RPCAuthReqMsg extends RPCReqBase<RPCAuthRspMsg> {

    public String authServerName = "";

    public String authIp = "";

    public String authPort = "";

    public String authMsg = "";

    @Override
    public String toString() {
        return "RPCAuthReqMsg{" +
                "authServerName='" + authServerName + '\'' +
                ", authIp='" + authIp + '\'' +
                ", authPort='" + authPort + '\'' +
                ", authMsg='" + authMsg + '\'' +
                ", sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}
