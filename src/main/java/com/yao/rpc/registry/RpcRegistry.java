package com.yao.rpc.registry;

import com.yao.rpc.serializable.MyProtocolDecode;
import com.yao.rpc.serializable.MyProtocolEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yaojian
 * @date 2022/1/11 21:57
 */
public class RpcRegistry {

    private int port;

    private RegistryHandler registryHandler = new RegistryHandler();


    public RpcRegistry(int port){
        this.port = port;
    }

    public void start(){

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //处理拆包粘包的解编码器
                    //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4));
                    //pipeline.addLast(new LengthFieldPrepender(4));

                    //处理序列化解编码器
                    //pipeline.addLast("encoder",new ObjectEncoder());
                    //pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast(new MyProtocolEncode());
                    pipeline.addLast(new MyProtocolDecode());
                    pipeline.addLast(registryHandler);

                    //业务拓展


                }
            });
            b.option(ChannelOption.SO_SNDBUF, 16*1024)
                    //设置接收消息缓冲区大小
                    .option(ChannelOption.SO_RCVBUF, 16*1024)
                    //是否保持心跳监听
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(this.port).sync();
            System.out.println("注册中心启动完成！");
            future.channel().closeFuture().sync();

        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {

        RpcRegistry rpcRegistry = new RpcRegistry(8088);
        rpcRegistry.start();

    }


}
