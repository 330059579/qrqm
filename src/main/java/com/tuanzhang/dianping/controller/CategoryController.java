package com.tuanzhang.dianping.controller;


import com.tuanzhang.dianping.common.CommonRes;
import com.tuanzhang.dianping.model.Category;
import com.tuanzhang.dianping.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller("/category")
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping("/list")
    public CommonRes list(){
        List<Category> categories = categoryService.selectAll();
        return CommonRes.create(categories);
    }
}
