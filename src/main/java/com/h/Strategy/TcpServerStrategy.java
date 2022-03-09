package com.h.Strategy;

import com.h.autoconfigure.NettyConfigProperties;
import com.h.util.NetUtil;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ：Hukaiwen
 * @description ：Tcp服务端引导类创建策略
 * @date ：2022/3/9 13:56
 */
public class TcpServerStrategy implements BootstrapStrategy<ServerBootstrap, ServerChannel> {
    @Override
    public AbstractBootstrap<ServerBootstrap, ServerChannel> createBootstrap(NettyConfigProperties.Configuration config, ChannelHandler ch) throws Exception {
        final NioEventLoopGroup boss = new NioEventLoopGroup();
        final NioEventLoopGroup worker = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(NetUtil.getInetSocketAddress(config.getLocalAddress(),config.getPort()))
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.SO_BACKLOG,1024)
                .childHandler(ch);
        return serverBootstrap;
    }
}
