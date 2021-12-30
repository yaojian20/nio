package com.yao.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author yaojian
 * @date 2021/12/28 22:35
 */
public class AIOServer {

    private Integer port;

    public AIOServer(Integer port) {
        this.port = port;
    }

    public void listen() throws IOException {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                result.read(buffer);
                buffer.flip();
                System.out.println(new String(buffer.array()));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        });
    }
}
