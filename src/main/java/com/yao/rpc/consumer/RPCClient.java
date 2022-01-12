package com.yao.rpc.consumer;

import com.yao.rpc.core.msg.InvokerMsg;
import com.yao.rpc.serializable.MyProtocolDecode;
import com.yao.rpc.serializable.MyProtocolEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.annotation.PreDestroy;

/**
 * Created by yaojian on 2022/1/12 10:55
 *
 * @author
 */
public class RPCClient {

    private String host;

    private Integer port;

    private EventLoopGroup group;

    private ChannelFuture future;

    private ClientHandler clientHandler = new ClientHandler();

    public RPCClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }


    public void connect(){
        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        //绑定线程
        bootstrap.group(this.group);
        //
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                //处理拆包粘包的解编码器
                //pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4));
                //pipeline.addLast(new LengthFieldPrepender(4));
                //处理序列化解编码器
                //pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                //pipeline.addLast("encoder",new ObjectEncoder());
                pipeline.addLast(new MyProtocolEncode());
                pipeline.addLast(new MyProtocolDecode());
                pipeline.addLast(clientHandler);
            }
        });
        try {
            this.future = bootstrap.connect(host,port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object send(InvokerMsg invokerMsg){
        //future.channel().writeAndFlush("lalalalalalala");
        future.channel().writeAndFlush(invokerMsg);
        Object result = clientHandler.getResult(invokerMsg.getRequestId());
        //future.channel().close();
        return result;
    }

    @PreDestroy
    public void close() {
        group.shutdownGracefully();
        future.channel().closeFuture().syncUninterruptibly();
    }



}
