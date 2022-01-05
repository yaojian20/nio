package com.yao.im;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author yaojian
 * @date 2022/1/4 22:14
 */
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    //
    private URL baseURL= HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    private final  String WEB_ROOT = "webroot";

    private File getFileFromRoot(String fileName) throws URISyntaxException {
        String path = baseURL.toURI() + WEB_ROOT + "/" + fileName;
        path = path.startsWith("file:")?path.substring(5):path;
        path = path.replaceAll("//","/");
        return new File(path);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        String url = request.uri();
        String page = url.equals("/")?"chat.html":url;
        RandomAccessFile file = new RandomAccessFile(getFileFromRoot(page),"r");
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        String contextType = "text/html";
        if (url.endsWith(".css")){
            contextType = "text/css";
        } else if (url.endsWith(".js")){
            contextType = "text/javascript";
        } else if (url.toLowerCase().matches("(jpg|png|gif)$")){
            String ext = url.substring(url.lastIndexOf("."));
            contextType = "/image" + ext +";";
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,contextType+"charset=utf-8");

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        if (keepAlive){
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        channelHandlerContext.write(response);
        channelHandlerContext.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
        ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        file.close();
        if (!keepAlive){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
