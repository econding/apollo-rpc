package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.core.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.core.session.executor.ResponseExecutor;

public class RPCAuthRspMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCAuthRspMsg>,RPCAuthRspMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCAuthRspMsg> requestMsgHolder) {
        new ResponseExecutor<RPCAuthRspMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
