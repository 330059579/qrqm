package com.tuanzhang.dianping.controller.admin;

import com.tuanzhang.dianping.common.AdminPermission;
import com.tuanzhang.dianping.model.Seller;
import com.tuanzhang.dianping.service.SellerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {

    @Resource
    private SellerService sellerService;


    //商户列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(){
        List<Seller> sellers = sellerService.selectAll();
        ModelAndView modelAndView = new ModelAndView("/admin/seller/index.html");
        modelAndView.addObject("data", sellers);
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }
}
