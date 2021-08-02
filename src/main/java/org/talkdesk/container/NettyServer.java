package org.talkdesk.container;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private static final int port = 6789; //设置服务端端口
    private static EventLoopGroup group = new NioEventLoopGroup();   // 通过nio方式来接收连接和处理连接
    private static  ServerBootstrap b = new ServerBootstrap();

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) {
        // 添加事件回环组
        b.group(group);
        // 默认套接字通道
        b.channel(NioServerSocketChannel.class);
        // 放过滤器
        b.childHandler(new NettyServerFilter());
        // 绑定端口
        try {
            ChannelFuture f = b.bind(port).sync();
            System.out.println("服务端口启动成功...");
            // 监听服务器关闭监听 ??
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
           // 关闭事件回环组,释放掉所有资源包括创建的线程
            group.shutdownGracefully();
        }
    }

}
