package com.yao.im;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yaojian on 2022/1/5 11:02
 *
 * @author
 */
@Data
public class MyProtocol implements Serializable {

    //操作
    private String operation;

    private Long dateTimeStamp;

    private String sendUserName;

    private String message;

    @Override
    public String toString() {
        return "MyProtocol{" +
                "operation='" + operation + '\'' +
                ", dateTimeStamp=" + dateTimeStamp +
                ", sendUserName='" + sendUserName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
