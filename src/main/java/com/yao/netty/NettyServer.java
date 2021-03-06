package com.yao.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by yaojian on 2021/12/30 14:47
 *
 * @author
 */
public class NettyServer {

    //创建线程组，监听客户端请求
    private EventLoopGroup acceptGroup;

    //负责处理客户端请求的线程组
    private EventLoopGroup handleGroup;

    //服务器启动相关配置
    private ServerBootstrap bootstrap;

    //端口号
    private Integer port;

    public static void main(String[] args){
        NettyServer nettyServer = new NettyServer(9090);
        ChannelFuture future = null;
        try {
            future = nettyServer.handle(new ServerHandler());
            System.out.println("server started.");
            //关闭连接，回收资源
            future.channel().closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (future != null){
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (nettyServer != null){
                nettyServer.release();
            }
        }
    }

    public NettyServer(Integer port) {
        this.port = port;
        acceptGroup = new NioEventLoopGroup();
        handleGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        //绑定监听线程组
        bootstrap.group(acceptGroup,handleGroup);
        //设置通道模式，同步非阻塞
        bootstrap.channel(NioServerSocketChannel.class);
        //设置缓冲区大写，字节数
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        //设置发送消息缓冲区大小
        bootstrap.option(ChannelOption.SO_SNDBUF, 16*1024)
                //设置接收消息缓冲区大小
                 .option(ChannelOption.SO_RCVBUF, 16*1024)
                //是否保持心跳监听
                 .option(ChannelOption.SO_KEEPALIVE, true);

    }

    public ChannelFuture handle(final ChannelHandler... acceptorHandlers) throws InterruptedException {
        /**
         * childHandler 是服务端的BootStrap独有的方法是用于提供处理对象，提供处理对象可以一次性的增加若干个处理逻辑
         * 类似责任链模式的处理逻辑，也就是说你增加A 和B两个处理逻辑，在处理逻辑的时候会按照A 和 B 的顺序进行依次处理
         *
         * ChannelInitializer 用于提供处理器的一个模型对象，这个模型对象，其中定义了一个方法initChannel
         *
         * initChannel 这个方法适用于初始化处理逻辑责任链条的。可以保证服务端的BootStrap只初始化一次处理器，尽量提供处理器的重用，
         * 减少了反复创建处理器的操作
         */
        //SocketChannel负责读写操作
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //无锁化串行编程
                // HttpResponseEncoder 编码器
                //socketChannel.pipeline().addLast(new HttpResponseEncoder());
                // HttpRequestDecoder 解码器
                //socketChannel.pipeline().addLast(new HttpRequestDecoder());
                //业务逻辑
                socketChannel.pipeline().addLast(acceptorHandlers);
            }
        });
        /**
         * bind 方法 用来绑定处理端口，ServerBootstrap可以绑定多个监听端口。多次调用bind方法即可
         *
         * sync 方法开始启动监听逻辑，返回ChannelFuture 返回结果是监听成功后的未来结果，可以使用
         * 这个ChannelFuture实现后续的服务器与客户端的交互所以要获取这个ChannelFuture对象
         */
        //同步阻塞
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        return channelFuture;
    }

    /**
     * 回收方法
     * shutdownGracefully 是一个安全关闭的方法，可以保证不放弃任何一个以接收的客户端请求
     */
    public void release(){
        this.acceptGroup.shutdownGracefully();
        this.handleGroup.shutdownGracefully();
    }

}
