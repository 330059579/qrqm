package com.tuanzhang.dianping.controller;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.CommonError;
import com.tuanzhang.dianping.common.CommonRes;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.model.User;
import com.tuanzhang.dianping.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller("/user")
@RequestMapping("/user")
public class UserController {

     @Resource
     private UserService userService;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "success";
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonRes getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        User user = userService.getUser(id);
        if(user == null){
            //return CommonRes.create(new CommonError(EmBusinessError.NO_OBJECT_FOUND),"fail");
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        }else{
            return CommonRes.create(user);
        }
    }

    @RequestMapping("/index")
    public ModelAndView index(){
        String userName = "tuanzhang";
        ModelAndView modelAndView = new ModelAndView("/index.html");
        modelAndView.addObject("name",userName);
        return modelAndView;
    }

}
