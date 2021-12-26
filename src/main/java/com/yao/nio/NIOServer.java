package com.yao.nio;

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
 * @author yaojian
 * @date 2021/12/26 21:16
 */
public class NIOServer {

    private int port;

    private Selector selector = null;

    private static String USER_CONTENT_SPILIT = "#@#";

    private Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws IOException {
        NIOServer nioServer = new NIOServer(8080);
        nioServer.listener();
    }

    public NIOServer(int port) throws IOException {
        this.port = port;

        //打开通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //选择器开始工作
        selector = Selector.open();
        //可以开始工作
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动，监听端口是：" + this.port);
    }

    public void listener() throws IOException {
        //死循环，这里不会堵塞
        //CPU工作频率可控了，是可控的固定值
        while (true){
            int wait = selector.select();
            if (wait == 0) continue;
            Set<SelectionKey> keys = selector.selectedKeys();//知道可用通道
            //取号
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //处理完一个，消除号码
                iterator.remove();
                process(selectionKey);
            }
        }
    }


    public void process(SelectionKey selectionKey) throws IOException {

        //判断客户端确定已经进入服务大厅宁琼恩已经连接好，可以实现交互了
        //表示有新的连接
        if (selectionKey.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
            SocketChannel client = server.accept();
            //非阻塞模式
            client.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求
            client.register(selector, SelectionKey.OP_READ);
            selectionKey.interestOps(SelectionKey.OP_ACCEPT);
            client.write(charset.encode("请输入你的昵称"));
        }
        //可以读的时候
        if (selectionKey.isReadable()){
            //通过key反向获取channel
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer readBuf = ByteBuffer.allocate(1024);
            //获取该channel关联的buffer
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            buffer.flip();
            channel.read(buffer);
            System.out.println("客户端收到消息：" + new String(buffer.array()));

        }
    }
}
