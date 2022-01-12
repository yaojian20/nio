package com.yao.rpc.registry;

import com.yao.rpc.core.msg.InvokerMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yaojian
 * @date 2022/1/11 22:17
 */
public class RegistryHandler extends SimpleChannelInboundHandler<InvokerMsg> {
    public RegistryHandler() {
        scanClass("com.yao.rpc.provider");
        doRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvokerMsg invokerMsg) throws Exception {
        System.out.println("注册中心借到请求 ：" + invokerMsg.toString());
        Object result = new Object();
        InvokerMsg request = invokerMsg;
        if (registryMap.containsKey(request.getClassName())){
            Object clazz = registryMap.get(request.getClassName());

            Method method = clazz.getClass().getMethod(request.getMethodName(),request.getParams());
            result = method.invoke(clazz, request.getValues());
            request.setResult(result);
            System.out.println("执行结果为：" + result);
            registryMap.put(request.getRequestId(),result);
        }
        ctx.writeAndFlush(request);
    }

    //需要一个容器充当注册中心

    public static ConcurrentHashMap<String, Object> registryMap  = new ConcurrentHashMap<>();


    public static List<String> classCache = new ArrayList<>();

    //自定义约定放在provider包里的对外开放


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    //扫描所有class
    private void scanClass(String packageName){
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.","/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()){

            if (file.isDirectory()){
                scanClass(packageName + "." + file.getName());
            } else {
                String className = packageName +"." +file.getName().replace(".class", "").trim();
                System.out.println("className is :" + className);
                classCache.add(className);
            }

        }

    }

    //注册过程
    //
    private void doRegistry(){
        if (classCache.isEmpty()){
            return;
        }
        for (String className : classCache){

            try {
                Class<?> clazz = Class.forName(className);
                //接口
                Class<?> interfaces = clazz.getInterfaces()[0];
                registryMap.put(interfaces.getName(),clazz.newInstance());
                System.out.println(registryMap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
