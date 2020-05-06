package com.tuanzhang.dianping.dal;

import com.tuanzhang.dianping.model.Category;
import com.tuanzhang.dianping.model.CategoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryDAO {
    int countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}