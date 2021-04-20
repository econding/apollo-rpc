package com.apollo.rpc.core.handler;

import com.apollo.rpc.core.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.core.msg.impl.RPCServerCheckRspMsg;
import com.apollo.rpc.core.session.executor.ResponseExecutor;

public class RPCServerCheckRspMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCServerCheckRspMsg>, RPCServerCheckRspMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCServerCheckRspMsg> requestMsgHolder) {
        new ResponseExecutor<RPCServerCheckRspMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
