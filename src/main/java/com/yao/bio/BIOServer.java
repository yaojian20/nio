package com.yao.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author yaojian
 * @date 2021/12/26 19:19
 */
public class BIOServer {
    private ServerSocket serverSocket;

    public BIOServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    private void listener() throws IOException {
        while (true){
            //等待客户端连接，阻塞方法
            Socket client = serverSocket.accept();
            InputStream inputStream = client.getInputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(bytes)) > 0 ){
                String message = new String(bytes,0,len);
                System.out.println("message is :" + message);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new BIOServer(8080).listener();
    }

}
