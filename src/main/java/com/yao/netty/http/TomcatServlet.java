package com.yao.netty.http;

import java.io.UnsupportedEncodingException;

/**
 * @author yaojian
 * @date 2021/12/30 23:01
 */
public class TomcatServlet extends Myservlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) {
        System.out.println("doGet.......");
        try {
            response.write(request.getParameter("name"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) {

    }
}
