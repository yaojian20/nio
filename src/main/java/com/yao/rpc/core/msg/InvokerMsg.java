package com.yao.rpc.core.msg;

import lombok.Data;

/**
 * @author yaojian
 * @date 2022/1/11 22:56
 */
public class InvokerMsg {

    //服务名称
    private String className;

    private String methodName;

    private Class<?>[] params;

    private Object[] values;

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
}
