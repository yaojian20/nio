package com.yao.netty.http;

/**
 * @author yaojian
 * @date 2021/12/30 22:29
 */
public abstract class Myservlet {

    public abstract void doGet(MyRequest request,MyResponse response);

    public abstract void doPost(MyRequest request,MyResponse response);

}
