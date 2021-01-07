package com.apollo.rpc.handler;

import com.apollo.rpc.handler.holder.ResponseMsgHolder;
import com.apollo.rpc.msg.impl.RPCResponseMsg;
import com.apollo.rpc.session.excutor.ResponseExcutor;

public class RPCResponseMsgHandler implements RPCMsgHandler<ResponseMsgHolder<RPCResponseMsg>,RPCResponseMsg> {

    @Override
    public void doHandle(ResponseMsgHolder<RPCResponseMsg> requestMsgHolder) {
        new ResponseExcutor<RPCResponseMsg>().doResponse(requestMsgHolder.getMsg());
    }

}
