package com.apollo.rpc.core.msg.impl;

import com.apollo.rpc.core.msg.RPCRspBase;

public class RPCServerCheckRspMsg extends RPCRspBase<RPCServerCheckReqMsg> {

    public boolean isAvailability = false;

    @Override
    public void init(RPCServerCheckReqMsg rpcServerCheckReqMsg) {
        super.init(rpcServerCheckReqMsg);
    }

    @Override
    public String toString() {
        return "RPCServerCheckRspMsg{" +
                "isAvailability=" + isAvailability +
                ", sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}
