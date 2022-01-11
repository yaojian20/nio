package com.yao.rpc.provider;

import com.yao.rpc.api.RpcInterface;

/**
 * @author yaojian
 * @date 2022/1/11 21:58
 */
public class RpcService implements RpcInterface {
    @Override
    public String sayHello(String name) {
        return "hello world : " + name;
    }
}
