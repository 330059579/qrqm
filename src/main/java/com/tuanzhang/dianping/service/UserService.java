package com.tuanzhang.dianping.service;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.model.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    User getUser(Integer id);

    User register(User user) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    User login(String telphone, String password)throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;
}
