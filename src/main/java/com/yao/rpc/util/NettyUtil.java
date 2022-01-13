package com.yao.rpc.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaojian on 2022/1/13 11:56
 *
 * @author
 */
public class NettyUtil {


    /**
     * 响应消息缓存
     */
    private static Cache<String, BlockingQueue<Object>> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(1000, TimeUnit.SECONDS)
            .build();


    /**
     * 等待响应消息
     * @param key 消息唯一标识
     * @return ReceiveDdcMsgVo
     */
    public static Object waitReceiveMsg(String key) {

        try {
            //设置超时时间
            Object vo = Objects.requireNonNull(responseMsgCache.getIfPresent(key))
                    .poll(5000, TimeUnit.MILLISECONDS);

            //删除key
            responseMsgCache.invalidate(key);
            return vo;
        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }

    }

    /**
     * 初始化响应消息的队列
     * @param key 消息唯一标识
     */
    public static void initReceiveMsg(String key) {
        responseMsgCache.put(key,new LinkedBlockingQueue<>(1));
    }

    /**
     * 设置响应消息
     * @param key 消息唯一标识
     */
    public static void setReceiveMsg(String key, Object msg) {

        if(responseMsgCache.getIfPresent(key) != null){
            responseMsgCache.getIfPresent(key).add(msg);
            return;
        }

        System.out.println(key + "不存在");
    }


}
