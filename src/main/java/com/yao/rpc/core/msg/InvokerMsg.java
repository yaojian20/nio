package com.yao.rpc.core.msg;

import lombok.Data;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author yaojian
 * @date 2022/1/11 22:56
 */
@Message
public class InvokerMsg implements Serializable {

    private String requestId;

    //接口名称
    private String className;

    //方法名
    private String methodName;

    //参数类型
    private Class<?>[] params;

    //参数值
    private Object[] values;

    //请求结果
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "InvokerMsg{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", values=" + Arrays.toString(values) +
                ", result=" + result +
                '}';
    }
}
