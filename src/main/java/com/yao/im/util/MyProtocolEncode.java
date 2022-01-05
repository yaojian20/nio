package com.yao.im.util;

import com.alibaba.fastjson.JSON;
import com.yao.im.MyProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by yaojian on 2022/1/5 11:11
 *
 * @author
 */
//编码器,将出站的数据转为byteBuf
@ChannelHandler.Sharable
public class MyProtocolEncode extends MessageToByteEncoder<MyProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyProtocol myProtocol, ByteBuf byteBuf) throws Exception {
        String message = JSON.toJSONString(myProtocol);
        System.out.println("encode message is :" + message);
        byteBuf.writeBytes(message.getBytes("utf-8"));
    }
}
