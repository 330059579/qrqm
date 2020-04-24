package com.tuanzhang.dianping.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ClobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRes doError(HttpServletRequest request, HttpServletResponse response, Exception ex){
        if (ex instanceof BusinessException) {
            return CommonRes.create(((BusinessException) ex).getCommonError(),"fail");
        }else if(ex instanceof NoHandlerFoundException){
            CommonError commonError = new CommonError(EmBusinessError.BIND_EXCEPTION_ERROR);
            return CommonRes.create(commonError,"fail");
        }else {
            CommonError commonError = new CommonError(EmBusinessError.UNKNOW_ERROR);
            return CommonRes.create(commonError);
        }
    }
}
