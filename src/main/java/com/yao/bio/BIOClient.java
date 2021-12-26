package com.yao.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author yaojian
 * @date 2021/12/26 19:17
 */
public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",8080);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello,I am socket client!".getBytes());
        socket.close();
    }

}
