package com.apollo.rpc.msg;


public abstract class RPCRspBase<T extends RPCReqBase> extends MsgBase {

    /**
     * 响应代码
     */
    public int responseCode = 0;

    public long responseTime = 0l;

    /**
     * 返回参数
     */
    public Object responseParameter = null;

    /**
     *
     * @param t
     */
    public void init(T t){
        sequenceNo = t.sequenceNo;
        msgType = t.msgType;
        serverName = t.serverName;
        instanceName = t.instanceName;
    }

}
