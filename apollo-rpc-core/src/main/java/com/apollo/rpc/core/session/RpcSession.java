package com.apollo.rpc.core.session;

import com.apollo.rpc.core.msg.RPCReqBase;

public interface RpcSession {

    <T extends RPCReqBase> Object request(T t);

}
