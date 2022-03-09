package com.h.autoconfigure;

import com.h.config.ChannelFactory;
import io.netty.bootstrap.Bootstrap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：Hukaiwen
 * @description ：netty自动启动自动配置类
 * @date ：2022/1/21 16:47
 */
@Configuration
@ConditionalOnClass(Bootstrap.class)
@EnableConfigurationProperties(NettyConfigProperties.class)
public class NettyAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public ChannelFactory channelFactory(){
        return new ChannelFactory();
    }
}
