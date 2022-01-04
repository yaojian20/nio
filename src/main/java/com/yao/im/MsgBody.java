package com.yao.im;

import lombok.Data;

/**
 * Created by yaojian on 2022/1/4 15:43
 *
 * @author
 */
@Data
//消息体
public class MsgBody {

    //发送人
    private String sendUserName;

    //消息体
    private String message;

}
