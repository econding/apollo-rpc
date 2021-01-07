package com.apollo.rpc.handler;

import com.apollo.rpc.handler.holder.MsgHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface RPCMsgHandler<T extends MsgHolder<M>, M> {

    Log log = LogFactory.getLog(RPCMsgHandler.class);

    void doHandle(T rpcRequest);

}
