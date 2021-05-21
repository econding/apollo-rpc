package com.apollo.rpc.core.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * 客户端连接发起的处理类
 */
public class Client {

    private static final Log log = LogFactory.getLog(Client.class);
    private static ChannelInitializer channelInitializer;
    private final String host;
    private final int port;
    public static Channel channel = null;

    public static void setChannelInitializer(ChannelInitializer channelInitializer) {
        Client.channelInitializer = channelInitializer;
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel connect() {
        if(channelInitializer == null){
            throw new NullPointerException("ChannelInitializer must not be null");
        }
        EventLoopGroup group;
        Channel channel = null;
        try {
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(channelInitializer);
            ChannelFuture f = b.connect().sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            log.error("unable to connect to the remote server:ip"+host+" port"+port);
        } finally {

        }
        return channel;
    }
}