# netty-starter

#### 介绍
netty springboot启动类

#### 软件架构
软件架构说明


#### 安装教程
Maven坐标
```xml
<dependencies>
    <dependency>
        <groupId>com.h</groupId>
        <artifactId>netty-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```


#### 使用说明
**@Handler中的value和configurations中的name必须相同才能匹配上**
```yml
netty:
  config:
    configurations:
      - name: myhandler1
        protocol: UDP_MULTICAST
        port: 9111
        localAddress: 10.243.83.49
        multicastAddress: 224.0.0.15
```
java代码
```java
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
```


#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
