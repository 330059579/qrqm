package com.tuanzhang.dianping.common;

public class CommonError {

    private Integer errCode;

    private String errorMsg;

    public CommonError(Integer errCode, String errorMsg) {
        this.errCode = errCode;
        this.errorMsg = errorMsg;
    }

    public CommonError(EmBusinessError emBusinessError){
        this.errCode = emBusinessError.getErrCode();
        this.errorMsg = emBusinessError.getErrMsg();
    }

    public Integer getErrCode() {
        return errCode;
    }


    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
