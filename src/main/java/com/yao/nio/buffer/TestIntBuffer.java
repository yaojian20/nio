package com.yao.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author yaojian
 * @date 2021/12/26 22:12
 */
public class TestIntBuffer {


    public static void main(String[] args) {
        //数组长度设为8
        IntBuffer intBuffer = IntBuffer.allocate(8);
        for (int i = 0; i< 5;i++){
            intBuffer.put(2*(i+1));
        }
        //https://blog.csdn.net/hbtj_1216/article/details/53129588
        //写模式转变为都模式，下标从0开始
        //也就是说调用flip()之后，读/写指针position指到缓冲区头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)。
        intBuffer.flip();
        intBuffer.put(1,100);
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }


}
