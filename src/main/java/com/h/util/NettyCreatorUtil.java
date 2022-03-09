package com.h.util;

import com.h.Strategy.*;
import com.h.api.TransferProtocol;
import com.h.autoconfigure.NettyConfigProperties;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：Hukaiwen
 * @description ：引导类创建工具类
 * @date ：2022/2/25 15:54
 */
public final class NettyCreatorUtil {

    private final static Logger log = LoggerFactory.getLogger(NettyCreatorUtil.class);

    private static final Map<Class<? extends BootstrapStrategy<?, ?>>, BootstrapStrategy<?, ?>> STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * @param : [com.h.autoconfigure.NettyConfigProperties.Configuration, io.netty.channel.ChannelHandler]
     * @return : io.netty.bootstrap.AbstractBootstrap<?,?>
     * @author : Hukaiwen
     * @description : 创建引导类
     * @date : 2022/3/9 15:39
     */
    public static AbstractBootstrap<?, ?> create(NettyConfigProperties.Configuration config, ChannelHandler ch) throws Exception {
        BootstrapStrategy<?, ?> bootstrapStrategy = null;
        //先尝试使用用户自定义的策略生成bootstrap
        if (config.getBootstrapStrategy() != null) {
            bootstrapStrategy = STRATEGY_MAP.computeIfAbsent(config.getBootstrapStrategy(), k -> {
                try {
                    return k.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(String.format("%s缺少无参构造函数或无权访问无参构造函数", k), e);
                    log.info("尝试使用内置引导类生成策略");
                    return getBootstrapStrategy(config);
                }
            });
        } else {
            bootstrapStrategy = getBootstrapStrategy(config);
        }

        return bootstrapStrategy.createBootstrap(config, ch);
    }

    /**
     * @param : [com.h.autoconfigure.NettyConfigProperties.Configuration]
     * @return : com.h.Strategy.BootstrapStrategy<?,?>
     * @author : Hukaiwen
     * @description : 根据配置自动生成引导类
     * @date : 2022/3/9 15:38
     */
    private static BootstrapStrategy<?, ?> getBootstrapStrategy(NettyConfigProperties.Configuration config) {
        BootstrapStrategy<?, ?> result = null;
        if (config.getProtocol() == TransferProtocol.UDP) {
            result = STRATEGY_MAP.computeIfAbsent(UdpServerStrategy.class, k -> new UdpServerStrategy());
        } else if (config.getProtocol() == TransferProtocol.UDP_MULTICAST) {
            result = STRATEGY_MAP.computeIfAbsent(UdpMulticastServerStrategy.class, k -> new UdpMulticastServerStrategy());
        } else if (config.getProtocol() == TransferProtocol.UDP_BROADCAST) {
            result = STRATEGY_MAP.computeIfAbsent(UdpBroadcastServerStrategy.class, k -> new UdpBroadcastServerStrategy());
        } else if (config.getProtocol() == TransferProtocol.TCP) {
            result = STRATEGY_MAP.computeIfAbsent(TcpServerStrategy.class, k -> new TcpServerStrategy());
        }
        return result;
    }
}
