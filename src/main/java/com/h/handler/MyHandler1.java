package com.h.handler;

import com.h.annotation.Handler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * @author ：Hukaiwen
 * @description：
 * @date ：2022/2/22 15:32
 */
@Handler("myhandler1")
public class MyHandler1 extends ChannelInitializer<NioDatagramChannel> {
    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                System.out.println(ctx.channel()+"已关闭");
                super.channelInactive(ctx);
            }

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                final ByteBuf content = msg.content();
                final String s = content.toString(CharsetUtil.UTF_8);
                System.out.println(s);
            }
        });
    }
}
