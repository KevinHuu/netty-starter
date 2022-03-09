package com.h.Strategy;

import com.h.autoconfigure.NettyConfigProperties;
import com.h.util.NetUtil;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.*;

/**
 * @author ：Hukaiwen
 * @description ：Udp组播服务端引导类生成策略
 * @date ：2022/3/8 10:57
 */
public class UdpMulticastServerStrategy implements BootstrapStrategy<Bootstrap,Channel> {
    @Override
    public AbstractBootstrap<Bootstrap, Channel> createBootstrap(NettyConfigProperties.Configuration config, ChannelHandler ch) throws Exception {
        final InetSocketAddress local = NetUtil.getInetSocketAddress(config.getLocalAddress(),config.getPort());
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channelFactory(() -> new NioDatagramChannel(InternetProtocolFamily.IPv4))
                .localAddress(local)
                .option(ChannelOption.IP_MULTICAST_IF, NetUtil.getNetworkInterface(local.getAddress()))
                .handler(ch);
        return bootstrap;
    }
}
