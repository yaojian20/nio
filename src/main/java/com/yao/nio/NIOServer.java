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

    /**
     * nio最重要的四个部分Buffer, channel, selector, charset
     * Buffer:即缓冲区，读写的数据都放在缓冲区中(数组的封装)
     * channel:通道 负责和缓冲区配合读写数据
     * selector: 选择器 负责监控通道中的IO情况 通过调用Selector.open()方法创建一个Selector。
     */

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
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);
        //创建选择器
        selector = Selector.open();
        //注册接收事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动，监听端口是：" + this.port);
    }

    public void listener() throws IOException {
        //死循环，这里不会堵塞
        //CPU工作频率可控了，是可控的固定值
        while (true){
            //监控所有注册的Channel，当它们中间有需要处理的IO操作时，该方法返回
            int wait = selector.select();
            //没有待处理的IO操作
            if (wait == 0) continue;
            Set<SelectionKey> keys = selector.selectedKeys();//知道可用通道
            //取号
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //处理一个IO操作，就删除该操作，防止重复处理
                iterator.remove();
                //进行IO操作处理
                process(selectionKey);
            }
        }
    }


    public void process(SelectionKey selectionKey) throws IOException {

        //表示有新的连接
        if (selectionKey.isAcceptable()){
            //获得当前server操作的服务端通道
            ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
            //获得新连接进来的客户端
            SocketChannel client = server.accept();
            //非阻塞模式
            client.configureBlocking(false);
            //注册选择器，并设置为读取模式，收到一个连接请求
            client.register(selector, SelectionKey.OP_READ);
            //selectionKey.interestOps(SelectionKey.OP_ACCEPT);
            System.out.println("server is ready!");
            client.write(charset.encode("请输入你的昵称"));
        }
        //可以读的时候
        if (selectionKey.isReadable()){
            //通过key反向获取channel
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //清空缓存数据
            buffer.clear();
            //读取数据
            int count = channel.read(buffer);
            byte[] bytes = new byte[count];
            //改为读模式
            buffer.flip();
            buffer.get(bytes);
            System.out.println("接收到客户端消息："+ new String(bytes));
        }
    }
}
