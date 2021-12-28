package com.yao.nio.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by yaojian on 2021/12/28 14:39
 *
 * @author
 */
public class NIOClient {

    private Integer port;

    private Selector selector;

    private SocketChannel socketChannel;

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    /**
     * 输入流
     */
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        new NIOClient(8080).start();
    }


    public NIOClient(Integer port) throws IOException {
        this.port = port;
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
        selector = Selector.open();
        //注册连接事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void start() throws IOException {
        while (true){
            int wait = selector.select();
            if (wait == 0){
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                process(key);
                iterator.remove();
            }
        }
    }

    public void process(SelectionKey key) throws IOException {
        if (key.isConnectable()){
            System.out.println("正在连接服务端。。。");
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.configureBlocking(false);
            socketChannel.finishConnect();
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
        if (key.isReadable()){
            buffer.clear();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            int count = socketChannel.read(buffer);
            buffer.flip();
            byte[] bytes = new byte[count];
            buffer.get(bytes);
            System.out.println("客户端收到服务端消息：" + new String(bytes));
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
        if (key.isWritable()){
            buffer.clear();
            System.out.println("请输入您的信息！");
            String requestLine = scanner.nextLine();
            buffer.put(requestLine.getBytes());
            buffer.flip();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.write(buffer);
        }
    }


}
