package com.yao.rpc.consumer;

import com.yao.im.util.MyProtocolDecode;
import com.yao.im.util.MyProtocolEncode;
import com.yao.rpc.core.msg.InvokerMsg;
import com.yao.rpc.serializable.InvokeMsgDecode;
import com.yao.rpc.serializable.InvokeMsgEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                pipeline.addLast(new InvokeMsgEncode());
                pipeline.addLast(new InvokeMsgDecode());
                pipeline.addLast(clientHandler);
            }
        });
        try {
            this.future = bootstrap.connect(host,port).sync();
            System.out.println("连接注册中心成功！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object send(InvokerMsg invokerMsg){
        this.future.channel().writeAndFlush("lalalalalalala");
        this.future.channel().writeAndFlush(invokerMsg);
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
