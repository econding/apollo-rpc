package com.apollo.rpc.core.channel;

import com.apollo.rpc.core.serializable.MarshallingCodeCFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;


public final class RPCChannelInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch){

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        pipeline.addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        pipeline.addLast(new RPCChannelInboundHandler());

    }

}
