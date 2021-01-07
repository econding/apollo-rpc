package com.apollo.rpc.msg.impl;

import com.apollo.rpc.msg.RPCReqBase;

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
