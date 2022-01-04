package com.yao.netty.http;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import javax.xml.ws.Response;

/**
 * @author yaojian
 * @date 2021/12/30 23:06
 */
@ChannelHandler.Sharable
public class TomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println(msg);
        if (msg instanceof HttpRequest){
            System.out.println("is hhtprequest!!!!!!!!!!!!!!");
            HttpRequest request = (HttpRequest) msg;
            MyRequest myRequest = new MyRequest(request, ctx);
            MyResponse myResponse = new MyResponse(request, ctx);
            new TomcatServlet().doGet(myRequest, myResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
