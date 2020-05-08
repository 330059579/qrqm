package com.tuanzhang.dianping.service;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.model.Category;

import java.util.List;

public interface CategoryService {

    Category create(Category category) throws BusinessException;

    Category get(Integer id);

    List<Category> selectAll();

    Integer countAllCategory();
}
