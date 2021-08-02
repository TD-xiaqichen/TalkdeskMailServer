package org.talkdesk.container;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        ph.addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        ph.addLast("decoder",new StringDecoder());
        // todo 服务端业务逻辑
        ph.addLast("handler",new NettyServerHandler());
    }

}
