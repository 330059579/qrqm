package com.tuanzhang.dianping.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tuanzhang.dianping.common.*;
import com.tuanzhang.dianping.model.Seller;
import com.tuanzhang.dianping.model.Shop;
import com.tuanzhang.dianping.request.PageQuery;
import com.tuanzhang.dianping.request.SellerCreateReq;
import com.tuanzhang.dianping.request.ShopCreateReq;
import com.tuanzhang.dianping.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller("/admin/shop")
@RequestMapping("/admin/shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    //商户列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());
        List<Shop> shops = shopService.selectAll();
        PageInfo<Shop> shopPageInfo = new PageInfo<>(shops);
        ModelAndView modelAndView = new ModelAndView("/admin/shop/index.html");
        modelAndView.addObject("data", shopPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }


    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView = new ModelAndView("/admin/shop/create.html");
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid ShopCreateReq shopCreateReq, BindingResult bindingResult) throws BusinessException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        Shop shop = new Shop();
        shop.setName(shopCreateReq.getName());
        shop.setIconUrl(shopCreateReq.getIconUrl());
        shop.setAddress(shopCreateReq.getAddress());
        shop.setCategoryId(shopCreateReq.getCategoryId());
        shop.setEndTime(shopCreateReq.getEndTime());
        shop.setStartTime(shopCreateReq.getStartTime());
        shop.setLatitude(shopCreateReq.getLatitude());
        shop.setRemarkScore(new BigDecimal(0.0));
        shop.setLongitude(shopCreateReq.getLongitude());
        shop.setPricePerMan(shopCreateReq.getPricePerMan());
        shop.setSellerId(shopCreateReq.getSellerId());
        shop.setTags(shopCreateReq.getTags());
        shopService.create(shop);
        return "redirect:/admin/shop/index";


    }

    /*@RequestMapping(value="down",method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes down(@RequestParam(value="id")Integer id) throws BusinessException {
        Seller sellerModel = sellerService.changeStatus(id,1);
        return CommonRes.create(sellerModel);
    }

    @RequestMapping(value="up",method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes up(@RequestParam(value="id")Integer id) throws BusinessException {
        Seller sellerModel = sellerService.changeStatus(id,0);
        return CommonRes.create(sellerModel);
    }*/
}
