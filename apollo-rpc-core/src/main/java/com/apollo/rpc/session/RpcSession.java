package com.apollo.rpc.session;

import com.apollo.rpc.msg.RPCReqBase;

public interface RpcSession {

    <T extends RPCReqBase> Object request(T t);

}
