package com.apollo.rpc.msg.impl;

import com.apollo.rpc.comm.Constant;
import com.apollo.rpc.msg.RPCReqBase;

import java.util.Arrays;

public class RPCRequestMsg extends RPCReqBase<RPCResponseMsg> {

    /**
     * 正常的rpc请求
     * @param method
     * @param serverName
     * @param rpcInterface
     * @param args
     */
    public RPCRequestMsg(String method, String serverName, String rpcInterface, Object[] args){
        this.requestParameter = args;
        this.serverInterface = rpcInterface;
        this.serverMethod = method;
        this.serverName = serverName;
        this.msgType = Constant.method_invocation;
    }

    /**
     * 接口名称
     */
    public String serverInterface = "";

    /**
     * 方法名称
     */
    public String serverMethod = "";

    /**
     * 请求参数
     */
    public Object[] requestParameter = null;

    @Override
    public String toString() {
        return "RPCRequestMsg{" +
                "serverInterface='" + serverInterface + '\'' +
                ", serverMethod='" + serverMethod + '\'' +
                ", requestParameter=" + Arrays.toString(requestParameter) +
                ", sequenceNo=" + sequenceNo +
                ", msgType='" + msgType + '\'' +
                ", serverName='" + serverName + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}