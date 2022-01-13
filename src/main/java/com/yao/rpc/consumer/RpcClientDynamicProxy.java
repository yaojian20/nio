package com.yao.rpc.consumer;

import com.yao.rpc.core.msg.InvokerMsg;
import com.yao.rpc.util.NettyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by yaojian on 2022/1/12 11:15
 *
 * @author
 */
public class RpcClientDynamicProxy <T> implements InvocationHandler {

    //所要代理的类
    private Class<T> clazz;

    public RpcClientDynamicProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //获得接口名
        String className = method.getDeclaringClass().getName();
        System.out.println("className is :" + className);
        //获得方法名
        String methodName = method.getName();
        //获得参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        InvokerMsg invokerMsg = new InvokerMsg();
        String requestId = UUID.randomUUID().toString();
        invokerMsg.setRequestId(requestId);
        invokerMsg.setClassName(className);
        invokerMsg.setMethodName(methodName);
        invokerMsg.setParams(parameterTypes);
        invokerMsg.setValues(args);
        //NettyUtil.initReceiveMsg(requestId);
        RPCClient rpcClient = new RPCClient("localhost",8088);
        rpcClient.connect();
        Object result = rpcClient.send(invokerMsg);
        //Object result = NettyUtil.waitReceiveMsg(requestId);
        return result;
    }
}
