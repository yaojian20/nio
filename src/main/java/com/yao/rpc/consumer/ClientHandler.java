package com.yao.rpc.consumer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yao.rpc.core.msg.InvokerMsg;
import com.yao.rpc.util.NettyUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaojian on 2022/1/12 11:05
 *
 * @author
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 响应消息缓存
     */
    private static Cache<String, BlockingQueue<Object>> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(1000, TimeUnit.SECONDS)
            .build();

    private final Map<String, Object> resultMaps = new ConcurrentHashMap<>();

    private final Object object = new Object();

    private static final Integer MAX_RETRY_TIMES = 3;

    private static final Integer RETRY_MILLS = 5;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.getClass());
        if (msg instanceof  InvokerMsg){
            InvokerMsg invokerMsg = (InvokerMsg) msg;
            String requestId = invokerMsg.getRequestId();
            resultMaps.put(requestId,invokerMsg.getResult());
            //NettyUtil.setReceiveMsg(requestId,invokerMsg.getResult());
            System.out.println(resultMaps);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public Object getResult(String requestId){
        for (int i = 0; i < MAX_RETRY_TIMES; i++) {
            if (resultMaps.containsKey(requestId)) {
                Object result = resultMaps.get(requestId);
                resultMaps.remove(requestId);
                return result;
            }
            try {
                System.out.println("睡5s");
                TimeUnit.SECONDS.sleep(RETRY_MILLS);
                System.out.println("睡好了");
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
        return null;
    }



}
