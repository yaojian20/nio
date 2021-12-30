package com.yao.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * @author yaojian
 * @date 2021/12/30 22:30
 */
public class MyRequest {

    private HttpRequest httpRequest;
    private ChannelHandlerContext ctx;

    public MyRequest(HttpRequest httpRequest, ChannelHandlerContext ctx) {
        this.httpRequest = httpRequest;
        this.ctx = ctx;
    }

    public String getUri(){
        return httpRequest.uri();
    }

    public String getMethod(){
        return httpRequest.getMethod().name();
    }

    public Map<String, List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.parameters();
    }

    public String getParameter(String name){
        Map<String, List<String>> params = getParameters();
        List<String> param = params.get(name);
        if (null != param){
            return param.get(0);
        }
        return null;
    }

}
