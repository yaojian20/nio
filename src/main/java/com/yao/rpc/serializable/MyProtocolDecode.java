package com.yao.rpc.serializable;

import com.alibaba.fastjson.JSON;
import com.yao.im.MyProtocol;
import com.yao.rpc.core.msg.InvokerMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by yaojian on 2022/1/5 10:42
 *
 * @author
 */
//解码器，将入站的数据转化为需要的数据类型
public class MyProtocolDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //decode中ByteBuf要读完，且List<Object>不为空才可以
        //我们这里采用json字符串传递
//        String message = in.toString(Charset.forName("utf-8"));
//        //将ByteBuf读取下标移动
//        in.skipBytes(in.readableBytes());
//        System.out.println("message is :" + message );
//        MessagePack messagePack = new MessagePack();
//        InvokerMsg invokerMsg = messagePack.read(message.getBytes(),InvokerMsg.class);
//        out.add(invokerMsg);
        //decode中ByteBuf要读完，且List<Object>不为空才可以
        //我们这里采用json字符串传递
        String message = in.toString(Charset.forName("utf-8"));
        //将ByteBuf读取下标移动
        in.skipBytes(in.readableBytes());
        System.out.println("message is :" + message );
        MyProtocol myProtocol = JSON.parseObject(message,MyProtocol.class);
        out.add(myProtocol);
    }
}
