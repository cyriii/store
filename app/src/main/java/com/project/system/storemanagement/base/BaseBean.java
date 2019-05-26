package com.project.system.storemanagement.base;

public class BaseBean<T> {
    public T data;
    // 00000 是正常  000001是失败  100000 权限不足需要登陆
    public String code;
    public String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
