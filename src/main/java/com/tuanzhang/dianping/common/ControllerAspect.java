package com.tuanzhang.dianping.common;

import com.tuanzhang.dianping.controller.admin.AdminController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Aspect
@Configuration
public class ControllerAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Around("execution(* com.tuanzhang.dianping.controller.admin.*.*(..)) " +
            "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object adminControllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AdminPermission annotation = method.getAnnotation(AdminPermission.class);
        if (annotation == null) {
            //无需登录的公共方法
            Object proceed = joinPoint.proceed();
            return proceed;
        }

        //有AdminPermission标签，需要验证是否登录
        String email = (String) httpServletRequest.getSession().getAttribute(AdminController.CURRENT_ADMIN_SESSION);
        if (StringUtils.isEmpty(email)) {
            if (annotation.produceType().equals("text/html")){
                httpServletResponse.sendRedirect("/admin/admin/loginpage");
                return null;
            }else {
                CommonError commonError = new CommonError(EmBusinessError.ADMON_SHOULT_LOGIN);
                return CommonRes.create(commonError, "fail");
            }
        }else {
            Object proceed = joinPoint.proceed();
             return proceed;
        }
    }
}
