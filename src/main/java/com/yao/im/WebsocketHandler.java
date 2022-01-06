package com.yao.im;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author yaojian
 * @date 2022/1/4 23:06
 */
@ChannelHandler.Sharable
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private AttributeKey<String> nick_name = AttributeKey.valueOf("nickName");
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame o) throws Exception {
        channelHandlerContext.attr(nick_name).getAndSet("tom");
        System.out.println(o.text());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //退出
        super.handlerRemoved(ctx);
    }
}
