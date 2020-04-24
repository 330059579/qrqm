package com.tuanzhang.dianping.dal;

import com.tuanzhang.dianping.model.User;
import com.tuanzhang.dianping.model.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}