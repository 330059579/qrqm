package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.dal.UserDAO;
import com.tuanzhang.dianping.model.User;
import com.tuanzhang.dianping.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;
    @Override
    public User getUser(Integer id) {
        return userDAO.selectByPrimaryKey(id);
    }
}
