package com.tuanzhang.dianping.controller;

import com.tuanzhang.dianping.common.*;
import com.tuanzhang.dianping.model.User;
import com.tuanzhang.dianping.request.LoginRequestReq;
import com.tuanzhang.dianping.request.RegisterReq;
import com.tuanzhang.dianping.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller("/user")
@RequestMapping("/user")
public class UserController {

    public static final String CURRENT_USER_SESSION = "current_user_session";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Resource
    private UserService userService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "success";
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonRes getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        User user = userService.getUser(id);
        if (user == null) {
            //return CommonRes.create(new CommonError(EmBusinessError.NO_OBJECT_FOUND),"fail");
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        } else {
            return CommonRes.create(user);
        }
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        String userName = "tuanzhang";
        ModelAndView modelAndView = new ModelAndView("/index.html");
        modelAndView.addObject("name", userName);
        return modelAndView;
    }


    @RequestMapping("/register")
    @ResponseBody
    public CommonRes register(@Valid @RequestBody RegisterReq registerReq, BindingResult bindingResult) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        User registerUser = new User();
        BeanUtils.copyProperties(registerReq, registerUser);
        User user = userService.register(registerUser);
        return CommonRes.create(user);
    }

    @RequestMapping("/login")
    @ResponseBody
    public CommonRes login(@Valid @RequestBody LoginRequestReq loginRequestReq, BindingResult bindingResult) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException{
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        User user = userService.login(loginRequestReq.getTelphone(), loginRequestReq.getPassword());
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION, user);
        return CommonRes.create(user);

    }


    @RequestMapping("/logout")
    @ResponseBody
    public CommonRes logout()throws Exception{
        httpServletRequest.getSession().invalidate();
        return CommonRes.create(null);

    }

    @RequestMapping("/getcurrentuser")
    @ResponseBody
    public CommonRes getCurrentUser()throws Exception{
        User user = (User)httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonRes.create(user);

    }
}
