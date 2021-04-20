package com.apollo.rpc.core.handler.holder;

import io.netty.channel.Channel;

public interface MsgHolder<M> {
    M getMsg();
    Channel getChannel();
}
