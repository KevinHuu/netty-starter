package com.h.Strategy;

import com.h.autoconfigure.NettyConfigProperties;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author ：Hukaiwen
 * @description ：Bootstrap生成策略
 * @date ：2022/2/28 15:04
 */
public interface BootstrapStrategy<B extends AbstractBootstrap<B,C>,C extends Channel> {

    Logger log = LoggerFactory.getLogger(BootstrapStrategy.class);

    /**
     * @param : [com.h.autoconfigure.NettyConfigProperties.Configuration, io.netty.channel.ChannelHandler]
     * @return : io.netty.bootstrap.AbstractBootstrap<B,C>
     * @author : Hukaiwen
     * @description : 创建Netty启动类
     * @date : 2022/3/9 17:27
     */
    AbstractBootstrap<B,C> createBootstrap(NettyConfigProperties.Configuration config, ChannelHandler ch) throws Exception;
}
