package com.yao.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yaojian on 2021/12/28 10:36
 *
 * @author
 */
public class NIOServer {

    private Integer port;

    private ServerSocketChannel server;

    private Selector selector;

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws IOException {
        new NIOServer(8080).listen();
    }

    public NIOServer(Integer port) throws IOException {
        this.port = port;
        //打开通道
        server = ServerSocketChannel.open();
        //绑定端口号
        server.bind(new InetSocketAddress(port));
        //设置非阻塞
        server.configureBlocking(false);
        //打开选择器
        selector = Selector.open();
        //绑定事件
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端已经就绪，端口为：" + this.port);
    }


    public void listen() throws IOException {
        while (true){
            //看看有多少事件正在等待处理
            int wait = selector.select();
            if (wait == 0){
                continue;
            }
            //获得当前的时间
            Set<SelectionKey> keys = selector.selectedKeys();
           Iterator<SelectionKey> iterator = keys.iterator();
           while (iterator.hasNext()){
               //获得当前事件
               SelectionKey key = iterator.next();
               process(key);
               //处理完之后，去掉该事件
               iterator.remove();
           }
        }
    }


    //处理事件
    public void process(SelectionKey key) throws IOException {
        //如果是有新连接进来
        if (key.isAcceptable()){
            //获得服务器通道
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            //获得客户端通道
            SocketChannel client = serverSocketChannel.accept();
            client.configureBlocking(false);
            client.register(selector,SelectionKey.OP_READ);
            System.out.println("有客户端连接进来");
            buffer.clear();
            buffer.put("欢迎来到聊天室，请输入您的昵称！".getBytes(charset));
            buffer.flip();
            client.write(buffer);
        }
        //如果是可读的话
        if (key.isReadable()){
            buffer.clear();
            //获得客户端通道
            SocketChannel client = (SocketChannel)key.channel();
            //获得消息长度
            int count = client.read(buffer);
            byte[] bytes = new byte[count];
            buffer.flip();
            //将缓冲区的数据写到bytes里
            buffer.get(bytes);
            System.out.println("收到消息" + new String(bytes));
            client.write(ByteBuffer.wrap("收到消息!!!!!".getBytes()));
        }
    }
}
