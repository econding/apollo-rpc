package com.apollo.rpc.handler;

import com.apollo.rpc.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.msg.impl.RPCServerCheckRspMsg;
import com.apollo.rpc.session.excutor.ResponseExcutor;

public class RPCServerCheckRspMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCServerCheckRspMsg>, RPCServerCheckRspMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCServerCheckRspMsg> requestMsgHolder) {
        new ResponseExcutor<RPCServerCheckRspMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
