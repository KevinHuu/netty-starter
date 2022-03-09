package com.h.config;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * @author ：Hukaiwen
 * @description ：获取ChannelFuture工厂
 * @date ：2022/3/7 14:59
 */
public class ChannelFactory {

    private final Map<String, FutureTask<ChannelFuture>> channels =  new ConcurrentHashMap<>();

    private final Map<String, ChannelHandler> handlers =  new ConcurrentHashMap<>();

    public Map<String, FutureTask<ChannelFuture>> getChannels() {
        return channels;
    }

    public Map<String, ChannelHandler> getHandlers() {
        return handlers;
    }

}
