package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.core.msg.impl.RPCResponseMsg;
import com.apollo.rpc.core.session.executor.ResponseExecutor;

public class RPCResponseMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCResponseMsg>,RPCResponseMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCResponseMsg> requestMsgHolder) {
        new ResponseExecutor<RPCResponseMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
