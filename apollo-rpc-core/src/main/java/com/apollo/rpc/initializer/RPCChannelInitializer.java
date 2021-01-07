package com.apollo.rpc.initializer;

import com.apollo.rpc.serializable.MarshallingCodeCFactory;
import com.apollo.rpc.service.RPCDispatchService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;


public final class RPCChannelInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch){

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        pipeline.addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        pipeline.addLast(new RPCChannelHandler());

    }

}
