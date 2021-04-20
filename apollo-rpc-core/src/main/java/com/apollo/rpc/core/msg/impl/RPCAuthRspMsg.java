package com.apollo.rpc.core.msg.impl;

import com.apollo.rpc.core.msg.RPCRspBase;

/**
 * 客户端认应答消息
 */
public class RPCAuthRspMsg extends RPCRspBase<RPCAuthReqMsg> {

    public boolean result = false;

    @Override
    public void init(RPCAuthReqMsg rpcAuthReqMsg) {
        super.init(rpcAuthReqMsg);
    }

    @Override
    public String toString() {
        return "RPCAuthRspMsg{" +
                "responseCode=" + responseCode +
                ", responseTime=" + responseTime +
                ", responseParameter=" + responseParameter +
                ", sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}
