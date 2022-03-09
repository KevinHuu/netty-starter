package com.h.Strategy;

import com.h.autoconfigure.NettyConfigProperties;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @author ：Hukaiwen
 * @description ：Udp广播服务端引导类生成策略
 * @date ：2022/3/9 13:54
 */
public class UdpBroadcastServerStrategy implements BootstrapStrategy<Bootstrap, Channel> {
    @Override
    public AbstractBootstrap<Bootstrap, Channel> createBootstrap(NettyConfigProperties.Configuration config, ChannelHandler ch) throws Exception {
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioDatagramChannel.class)
                .localAddress(new InetSocketAddress(config.getLocalAddress(),config.getPort()))
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(ch);
        return bootstrap;
    }
}
