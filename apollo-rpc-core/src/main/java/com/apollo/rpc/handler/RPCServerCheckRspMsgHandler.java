package com.apollo.rpc.handler;

import com.apollo.rpc.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.msg.impl.RPCServerCheckRspMsg;
import com.apollo.rpc.session.executor.ResponseExecutor;

public class RPCServerCheckRspMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCServerCheckRspMsg>, RPCServerCheckRspMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCServerCheckRspMsg> requestMsgHolder) {
        new ResponseExecutor<RPCServerCheckRspMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
