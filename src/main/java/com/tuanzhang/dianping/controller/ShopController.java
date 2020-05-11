package com.tuanzhang.dianping.controller;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.CommonRes;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.model.Shop;
import com.tuanzhang.dianping.service.CategoryService;
import com.tuanzhang.dianping.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private ShopService shopService;
    @Resource
    private CategoryService categoryService;


    //推荐1.0
    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam BigDecimal longitude, @RequestParam BigDecimal latitude) throws BusinessException {
        if (null == latitude || null == longitude) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        List<Shop> shopList = shopService.recommend(longitude, latitude);
        return CommonRes.create(shopList);
    }

    //搜索服务V1.0
    @RequestMapping("/search")
    @ResponseBody
    public CommonRes search(@RequestParam(name="longitude")BigDecimal longitude,
                            @RequestParam(name="latitude")BigDecimal latitude,
                            @RequestParam(name="keyword")String keyword,
                            @RequestParam(name="orderby",required = false)Integer orderby,
                            @RequestParam(name="categoryId",required = false)Integer categoryId,
                            @RequestParam(name="tags",required = false)String tags) throws BusinessException {
        if(StringUtils.isEmpty(keyword) || longitude == null || latitude == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }


        

    }
}
