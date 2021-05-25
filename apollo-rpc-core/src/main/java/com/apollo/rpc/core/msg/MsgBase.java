package com.apollo.rpc.core.msg;

import java.io.Serializable;

public abstract class MsgBase implements Serializable {

    private static final long serialVersionUID = 6324541698551729830L;

    /**
     * 序列号
     */
    public long sequenceNo = 0;

    /**
     * 消息类型
     */
    public String msgType = "";

    /**
     *服务名称
     */
    public String serverName = "";

    /**
     * 消息实际发送的实例名称
     */
    public String instanceName = "";


    /**
     * 强制所有子类实现 toString 方法
     * @return
     */
    public abstract String toString();

}
