package com.yao.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * @author yaojian
 * @date 2021/12/30 22:30
 */
public class MyResponse {

    private HttpRequest httpRequest;
    private ChannelHandlerContext ctx;

    public MyResponse(HttpRequest httpRequest, ChannelHandlerContext ctx) {
        this.httpRequest = httpRequest;
        this.ctx = ctx;
    }

    public void write(String out) throws UnsupportedEncodingException {
        try {
            if (null == out) return;
            System.out.println("begin write!!!!!!!!!!!!!!!!");
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.wrappedBuffer(out.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE ,"text/json");
            response.headers().set(CONTENT_LENGTH ,response.content().readableBytes());
            response.headers().set(EXPIRES ,0);
            if (HttpHeaders.isKeepAlive(httpRequest)){
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
        } finally {
            ctx.flush();
        }


    }

}
