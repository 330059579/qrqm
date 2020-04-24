package com.tuanzhang.dianping.common;

public enum  EmBusinessError {

    //资源不存在
    NO_OBJECT_FOUND(10001,"请求对象不存在"),
    UNKNOW_ERROR(10002,"未知错误"),
    BIND_EXCEPTION_ERROR(10004,"请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005,"请求参数校验失败");

    private Integer errCode;

    private String errMsg;

    EmBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrorCode() {
        return errCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errCode = errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
