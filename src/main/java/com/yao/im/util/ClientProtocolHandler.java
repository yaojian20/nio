package com.yao.im.util;

import com.yao.im.MyProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by yaojian on 2022/1/5 11:54
 *
 * @author
 */
@ChannelHandler.Sharable
public class ClientProtocolHandler extends SimpleChannelInboundHandler<MyProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyProtocol myProtocol) throws Exception {
        System.out.println(myProtocol.toString());
    }
}
