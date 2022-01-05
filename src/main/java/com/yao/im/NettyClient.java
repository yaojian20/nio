package com.yao.im;

import com.yao.im.util.ClientProtocolHandler;
import com.yao.im.util.MyProtocolDecode;
import com.yao.im.util.MyProtocolEncode;
import com.yao.im.util.ServerProtocolHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

/**
 * Created by yaojian on 2022/1/5 11:39
 *
 * @author
 */
public class NettyClient {
    //private static final MyProtocolDecode MY_PROTOCOL_DECODE = new MyProtocolDecode();

    private Integer port;

    private String address;

    private EventLoopGroup eventLoopGroup;

    private Bootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException {
        NettyClient nettyClient = new NettyClient(9090,"localhost");
        nettyClient.start();
    }

    public NettyClient(Integer port, String address){
        this.port = port;
        this.address = address;
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        //绑定处理线程
        bootstrap.group(eventLoopGroup);
        //设置通道
        bootstrap.channel(NioSocketChannel.class);
    }

    public void start() throws InterruptedException {
        //设置处理器
        try {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new MyProtocolEncode());
                    socketChannel.pipeline().addLast(new MyProtocolDecode());
                    socketChannel.pipeline().addLast(new ClientProtocolHandler());
                }
            });
            ChannelFuture future = bootstrap.connect(address,port).sync();
            MyProtocol myProtocol = new MyProtocol();
            myProtocol.setOperation("init");
            myProtocol.setDateTimeStamp((new Date()).getTime());
            myProtocol.setSendUserName("system");
            myProtocol.setMessage("正在初始化！");
            System.out.println(1111111111);
            future.channel().writeAndFlush(myProtocol);
            //future.channel().close();
        } finally {
            //eventLoopGroup.shutdownGracefully();
        }

    }
}
