package com.yao.rpc.consumer;

import java.lang.reflect.Proxy;

/**
 * Created by yaojian on 2022/1/12 14:18
 *
 * @author
 */
//创建代理类返回实体类
public class ProxyFactory {

    public static <T> T create(Class<T> interfaceClass) throws Exception {
        //通过代理类生成对应class
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[] {interfaceClass}, new RpcClientDynamicProxy<T>(interfaceClass));
    }

}
