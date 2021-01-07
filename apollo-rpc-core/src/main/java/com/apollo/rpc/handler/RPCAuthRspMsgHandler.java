package com.apollo.rpc.handler;

import com.apollo.rpc.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.msg.impl.RPCAuthRspMsg;
import com.apollo.rpc.session.excutor.ResponseExcutor;

public class RPCAuthRspMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCAuthRspMsg>,RPCAuthRspMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCAuthRspMsg> requestMsgHolder) {
        new ResponseExcutor<RPCAuthRspMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
