package com.yao.netty.http;

import com.yao.netty.NettyServer;
import com.yao.netty.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

/**
 * @author yaojian
 * @date 2021/12/30 23:05
 */
public class MyTomcat {
    //创建线程组，监听客户端请求
    private EventLoopGroup acceptGroup;

    //负责处理客户端请求的线程组
    private EventLoopGroup handleGroup;

    //服务器启动相关配置
    private ServerBootstrap bootstrap;

    //端口号
    private Integer port;

    public static void main(String[] args){
        MyTomcat nettyServer = new MyTomcat(8888);
        ChannelFuture future = null;
        try {
            future = nettyServer.handle(new TomcatHandler());
            System.out.println("server started.");
            //关闭连接，回收资源
            future.channel().closeFuture().sync();
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

    public void test(){
        int port = 8888;
        //Netty封装了NIO，Reactor模型，Boss，worker
// Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
// Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Netty服务
            //ServetBootstrap   ServerSocketChannel
            ServerBootstrap server = new ServerBootstrap();
            // 链路式编程
            server.group(bossGroup, workerGroup)
                    // 主线程处理类,看到这样的写法，底层就是用反射
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类 , Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        protected void initChannel(SocketChannel client) throws Exception {
                            // 无锁化串行编程
                            //Netty对HTTP协议的封装，顺序有要求
                            // HttpResponseEncoder 编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            // HttpRequestDecoder 解码器
                            client.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            client.pipeline().addLast(new TomcatHandler());
                        }
                    })
                    // 针对主线程的配置 分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程的配置 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture f = server.bind(port).sync();
            System.out.println("Tomcat 已启动，监听的端口是：" + port);
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public MyTomcat(Integer port) {
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
            protected void initChannel(SocketChannel client) throws Exception {
// 无锁化串行编程
                //Netty对HTTP协议的封装，顺序有要求
                // HttpResponseEncoder 编码器
                client.pipeline().addLast(new HttpResponseEncoder());
                // HttpRequestDecoder 解码器
                client.pipeline().addLast(new HttpRequestDecoder());
                // 业务逻辑处理
                //业务逻辑
                client.pipeline().addLast(acceptorHandlers);
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
