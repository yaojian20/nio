package com.yao.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by yaojian on 2021/12/30 16:29
 *
 * @author
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel id is :" + ctx.channel().id().asLongText());
        //将 msg 转成一个 ByteBuf
        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(buf.toString(CharsetUtil.UTF_8));

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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel id is :" + ctx.channel().id().asLongText());
        super.channelActive(ctx);
    }
}
