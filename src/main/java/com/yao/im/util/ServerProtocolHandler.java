package com.yao.im.util;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.yao.im.MyProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by yaojian on 2022/1/5 11:20
 *
 * @author
 */
@ChannelHandler.Sharable
public class ServerProtocolHandler extends SimpleChannelInboundHandler<MyProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyProtocol message) throws Exception {
        System.out.println("ServerProtocolHandler reading .......");
        System.out.println(message.toString());
    }
}
