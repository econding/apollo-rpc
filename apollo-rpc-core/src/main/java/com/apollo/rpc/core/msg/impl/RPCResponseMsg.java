package com.apollo.rpc.core.msg.impl;


import com.apollo.rpc.core.msg.RPCRspBase;

public class RPCResponseMsg extends RPCRspBase<RPCRequestMsg> {

    /**
     * 接口名称
     */
    public String serverInterface = "";

    /**
     * 方法名称
     */
    public String serverMethod = "";


    @Override
    public void init(RPCRequestMsg rpcRequestMsg) {
        super.init(rpcRequestMsg);
        this.serverInterface = rpcRequestMsg.serverInterface;
        this.serverMethod = rpcRequestMsg.serverMethod;
    }

    @Override
    public String toString() {
        return "RPCResponseMsg{" +
                "serverInterface='" + serverInterface + '\'' +
                ", serverMethod='" + serverMethod + '\'' +
                ", sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}
