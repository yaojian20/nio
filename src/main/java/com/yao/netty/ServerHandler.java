package com.yao.netty;

/**
 * Created by yaojian on 2021/12/30 15:33
 *
 * @author
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端业务处理逻辑
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    //用来识别输入姓名
    private static final String CODE = "==||==";

    private final static ChannelGroup channels = new DefaultChannelGroup(new DefaultEventExecutorGroup(1).next());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将 msg 转成一个 ByteBuf
        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        String message = buf.toString(CharsetUtil.UTF_8);
        System.out.println("message is :" + message);
        if (message.contains(CODE)){
            //这个信息是用户名
            int index = message.indexOf(CODE);
            String name = message.substring(CODE.length(),message.length());
            System.out.println("name is :" + name);
            if (!channels.contains(ctx.channel())){
                channels.add(ctx.channel());
                ctx.writeAndFlush(Unpooled.copiedBuffer(name+"注册成功！", CharsetUtil.UTF_8));
            }
        } else {
            ctx.writeAndFlush(Unpooled.copiedBuffer("已收到消息：" + message, CharsetUtil.UTF_8));
            sendOthers(ctx, message);
        }

        /**
         * 如果调用的是write方法，不会刷新缓存，缓存中的数据不会发送到客户端，必须调用flush方法进行强制刷出
         *  ctx.write(msg);
         *  ctx.flush();
         */
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write + flush
        //将数据写到缓存，并刷新
        //一般来讲，我们会对发送的数据进行编码
        //ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端  ", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理逻辑
     * ChannelHandlerContext关闭代表当前与客户端的连接处理逻辑，当客户端异常退出的时候这个异常也会执行
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("server exceptionCaught method run …… ");
        ctx.close();
    }

    public void sendOthers(ChannelHandlerContext context, String message){
        if (!channels.isEmpty()){
            for (Channel channel : channels){
                if (!context.channel().equals(channel)){
                    channel.writeAndFlush(message);
                }
            }
        }

    }

}
