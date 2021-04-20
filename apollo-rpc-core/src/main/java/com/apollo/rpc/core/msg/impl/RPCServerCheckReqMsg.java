package com.apollo.rpc.core.msg.impl;

import com.apollo.rpc.core.msg.RPCReqBase;

public class RPCServerCheckReqMsg extends RPCReqBase<RPCServerCheckRspMsg> {
    @Override
    public String toString() {
        return "RPCServerCheckReqMsg{" +
                "sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}
