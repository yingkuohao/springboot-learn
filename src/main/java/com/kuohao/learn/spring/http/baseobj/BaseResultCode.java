package com.kuohao.learn.spring.http.baseobj;

import java.io.Serializable;

public enum BaseResultCode implements Serializable {
    FAILURE(0, "failure"),
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "系统内部异常"),

    REQUEST_FREQUEENTLY(4, "请求过于频繁"),
    ILLEGAL_PARAM(5, "请求参数非法"),
    UN_LOGIN(6, "未登录"),
    APP_ERROR(7, "appkey非法"),
    ACCESS_ERROR(8, "未授权"),
    SIGNATURE_ERROR(9, "接口签名非法"),
    DATA_EMPTY(10, "结果为空");


    private int code;
    private String msg;

    BaseResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccuss() {
        return this.getCode() == OK.getCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("code: ").append(code);
        sb.append("  ").append(msg);

        return sb.toString();
    }
}
