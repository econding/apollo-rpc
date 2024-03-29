package com.apollo.rpc.core.channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class Server {

    private ChannelInitializer channelInitializer;
    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public Channel start(int port) {
        if(channelInitializer == null){
            throw new NullPointerException("channelInitializer");
        }
        InetSocketAddress address = new InetSocketAddress(port);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer);
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return channel;
    }

    public void setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

}
