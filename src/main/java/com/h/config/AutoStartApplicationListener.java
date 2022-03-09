package com.h.config;

import com.h.annotation.Handler;
import com.h.api.TransferProtocol;
import com.h.autoconfigure.NettyConfigProperties;
import com.h.util.NetUtil;
import com.h.util.NettyCreatorUtil;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * @author ：Hukaiwen
 * @description ：监听容器刷新完成事件，扫描所有@Handler注释的bean对象，创建服务端
 * @date ：2022/2/22 14:07
 */
public class AutoStartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger log = LoggerFactory.getLogger(AutoStartApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Map<String, Object> handlerBeans = event.getApplicationContext().getBeansWithAnnotation(Handler.class);
        final NettyConfigProperties nettyConfigProperties = event.getApplicationContext().getBean(NettyConfigProperties.class);
        final ChannelFactory channelFactory = event.getApplicationContext().getBean(ChannelFactory.class);
        handlerBeans.forEach((k, v) -> {
            if (v instanceof ChannelHandler) {
                final String name = v.getClass().getAnnotation(Handler.class).value();
                final List<NettyConfigProperties.Configuration> configs = nettyConfigProperties.getConfigurations().stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
                if (configs.size() == 0) {
                    log.error(String.format("=====未找到Handler：%s对应的配置=====", name));
                    return;
                } else if (configs.size() > 1) {
                    log.error(String.format("=====找到Handler：%s对应的多条配置，请检查配置文件是否正确=====", name));
                    return;
                }
                NettyConfigProperties.Configuration config = configs.get(0);
                if (!config.isAvailable()) {
                    return;
                }
                AbstractBootstrap<?,?> bootstrap = null;
                //创建Bootstrap
                try {
                    bootstrap = NettyCreatorUtil.create(config,(ChannelHandler) v);
                } catch (UnknownHostException e) {
                    log.error(String.format("无法解析的主机地址:%s", config), e);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(String.format("%s缺少无参构造函数或无权访问无参构造函数", config.getBootstrapStrategy()), e);
                } catch (Exception e) {
                    log.error("创建引导类时发生错误", e);
                }
                if (bootstrap == null) {
                    return;
                }
                final AbstractBootstrap<?,?> finalBootstrap = bootstrap;

                final FutureTask<ChannelFuture> task = new FutureTask<>(() -> {
                    final ChannelFuture channelFuture = finalBootstrap.bind();
                    if (config.getProtocol() == TransferProtocol.UDP_MULTICAST){
                        channelFuture.addListener((ChannelFutureListener) bindFuture -> {
                            InetSocketAddress multicast = NetUtil.getInetSocketAddress(config.getMulticastAddress(),config.getPort());
                            NetworkInterface networkInterface = NetUtil.getNetworkInterface(NetUtil.getInetAddress(config.getLocalAddress()));
                            ((NioDatagramChannel) bindFuture.channel()).joinGroup(multicast,networkInterface).sync();
                            log.info(String.format("=====名称：%s，成功监听%s端口，成功加入组播地址：%s，传输协议：%s=====", config.getName(), config.getPort(),multicast, config.getProtocol().name()));
                            final ChannelFuture cf = bindFuture.channel().closeFuture();
                            cf.addListener((ChannelFutureListener) closeFuture -> {
                                ((NioDatagramChannel) closeFuture.channel()).leaveGroup(multicast,networkInterface).sync();
                                log.info(String.format("=====名称：%s，离开组播地址：%s，传输协议：%s=====", config.getName(), multicast, config.getProtocol().name()));
                                finalBootstrap.config().group().shutdownGracefully();
                            });
                        });
                    }
                    else {
                        channelFuture.addListener((ChannelFutureListener) bindFuture ->{
                            log.info(String.format("=====名称：%s，成功监听%s端口，传输协议：%s=====", config.getName(), config.getPort(), config.getProtocol().name()));
                            bindFuture.channel().closeFuture().addListener((ChannelFutureListener) closeFuture -> finalBootstrap.config().group().shutdownGracefully());
                        });
                    }
                    return channelFuture;
                });
                //自动启动服务端
                if (config.isAutoStart()) {
                    Executors.newSingleThreadExecutor().submit(task);
                } else {
                    log.info(String.format("=====名称：%s，尚未监听%s端口，等待线程启动，传输协议：%s=====", config.getName(), config.getPort(), config.getProtocol().name()));
                }
                if (channelFactory.getChannels().containsKey(name)) {
                    log.error(String.format("找到多个相同名称的Handler：%s，请检查@Handler注解", name));
                } else {
                    channelFactory.getChannels().put(name, task);
                    channelFactory.getHandlers().put(name, (ChannelHandler) v);
                }
            }
        });
    }
}
