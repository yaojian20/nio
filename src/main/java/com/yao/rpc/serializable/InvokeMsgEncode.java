package com.yao.rpc.serializable;

import com.alibaba.fastjson.JSON;
import com.yao.im.MyProtocol;
import com.yao.rpc.core.msg.InvokerMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by yaojian on 2022/1/5 11:11
 *
 * @author
 */
//编码器,将出站的数据转为byteBuf
@ChannelHandler.Sharable
public class InvokeMsgEncode extends MessageToByteEncoder<InvokerMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, InvokerMsg invokerMsg, ByteBuf byteBuf) throws Exception {
        System.out.println("encode :" + invokerMsg.toString());
        //MessagePack messagePack = new MessagePack();
        //byte[] bytes = messagePack.write(invokerMsg);
        //System.out.println("bytes is :" + bytes);
        //byteBuf.writeBytes(new MessagePack().write(invokerMsg));
        String message = JSON.toJSONString(invokerMsg);
        System.out.println("encode message is :" + message);
        byteBuf.writeBytes(message.getBytes("utf-8"));
    }
}
