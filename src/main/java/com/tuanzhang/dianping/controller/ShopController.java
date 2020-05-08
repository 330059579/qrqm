package com.tuanzhang.dianping.controller;

import com.tuanzhang.dianping.common.CommonRes;
import com.tuanzhang.dianping.service.CategoryService;
import com.tuanzhang.dianping.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private ShopService shopService;
    @Resource
    private CategoryService categoryService;


    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam BigDecimal longtitude, @RequestParam BigDecimal latitude) {

    }
}
