package org.talkdesk.container;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("服务端接受的消息:"+msg);
        if("quit".equals(msg)){ //服务端断开的条件
            ctx.close();
        }
        Date date = new Date();
        //返回客户端消息
        ctx.writeAndFlush(date+"\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:"+ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端"+ InetAddress.getLocalHost().getHostName());
        super.channelActive(ctx);
    }

}
