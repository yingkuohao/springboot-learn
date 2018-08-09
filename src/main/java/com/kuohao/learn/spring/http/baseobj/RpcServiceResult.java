package com.kuohao.learn.spring.http.baseobj;

import java.io.Serializable;

/**
 *
 *     在接口调用成功时，设置success为true,否则设为false.
 *
 * @param <T>
 */
public class RpcServiceResult<T> implements Serializable {

    private static final long serialVersionUID = 5940060270812930098L;
    private boolean success;

    private int code;
    private String msg;
    private String params;


    protected T datas;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public void setSuccessResult() {
        this.success = true;
        this.setCode(BaseResultCode.OK.getCode());
        this.setMsg(BaseResultCode.OK.getMsg());
    }

    public RpcServiceResult() {
    }

    public RpcServiceResult(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    private static RpcServiceResult successResult = new RpcServiceResult(true, BaseResultCode.OK.getCode(), BaseResultCode.OK.getMsg());
    private static RpcServiceResult failureResult = new RpcServiceResult(false, BaseResultCode.FAILURE.getCode(), BaseResultCode.FAILURE.getMsg());

    public static RpcServiceResult getSuccessInstance() {
        return successResult;
    }

    public static RpcServiceResult getFailureInstance() {
        return failureResult;
    }

    public void setCommonResultCode(BaseResultCode baseResultCode) {
        if (BaseResultCode.OK.equals(baseResultCode)) {
            this.setSuccess(true);
        } else {
            this.setSuccess(false);
        }
        this.setCode(baseResultCode.getCode());
        this.setMsg(baseResultCode.getMsg());
    }

    public void setFailureResult() {
        this.setSuccess(false);
        this.setCode(BaseResultCode.FAILURE.getCode());
        this.setMsg(BaseResultCode.FAILURE.getMsg());
    }


    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
