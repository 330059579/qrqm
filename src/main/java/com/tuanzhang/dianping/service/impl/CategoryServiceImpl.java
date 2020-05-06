package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.dal.CategoryDAO;
import com.tuanzhang.dianping.model.Category;
import com.tuanzhang.dianping.model.CategoryExample;
import com.tuanzhang.dianping.service.CategoryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDAO categoryDAO;

    @Override
    @Transactional
    public Category create(Category category) throws BusinessException {
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        try {
            categoryDAO.insertSelective(category);
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.CATEGORY_NAME_DUPLOCATED);
        }
        return category;
    }

    @Override
    public Category get(Integer id) {
        return categoryDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> selectAll() {
        CategoryExample example = new CategoryExample();
        return categoryDAO.selectByExample(example);
    }
}
