package com.yao.rpc.consumer;

import com.yao.rpc.api.RpcInterface;

import java.net.InetSocketAddress;

/**
 * @author yaojian
 * @date 2022/1/11 21:57
 */
public class RpcConsumer {


    public static void main(String[] args) throws Exception {

        RpcInterface rpcInterface = ProxyFactory.create(RpcInterface.class);
        String result = rpcInterface.sayHello("jack");
        System.out.println("result is :" + result);


    }


}
