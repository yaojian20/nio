package com.yao.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by yaojian on 2021/12/30 14:47
 *
 * @author
 */
public class NettyClient1 {
    private static final String CODE = "==||==";

    //处理请求线程
    private EventLoopGroup handleGroup;

    //客户端配置
    private Bootstrap bootstrap;

    private String address;

    private Integer port;

    public static void main(String[] args) {
        NettyClient1 nettyClient = new NettyClient1("localhost",9090);
        ChannelFuture future = null;
        try {
            future = nettyClient.handler(new ClientHandler());
            System.out.println("欢迎来到netty世界！请输入你的昵称！");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            future.channel().writeAndFlush(Unpooled.copiedBuffer((CODE+name).getBytes("UTF-8")));
            while (true){
                String message = scanner.nextLine();
                future.channel().writeAndFlush(Unpooled.copiedBuffer(message.getBytes("UTF-8")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (future != null){
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (nettyClient != null){
                nettyClient.release();
            }
        }

    }

    public NettyClient1(String address, Integer port) {
        this.address = address;
        this.port = port;
        handleGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        //定义线程组
        bootstrap.group(handleGroup);
        //设置通道
        bootstrap.channel(NioSocketChannel.class);
    }


    public ChannelFuture handler(final ChannelHandler ... handlers) throws InterruptedException {
        //SocketChannel负责读写操作
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(handlers);
            }
        });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(address, port)).addListener(a->{
            if (a.isSuccess()){
                System.out.println("绑定成功！");
            } else {
                System.out.println("绑定失败！");
            }
        }).sync();
        return future;
    }

    public void release(){
        this.handleGroup.shutdownGracefully();
    }




}
