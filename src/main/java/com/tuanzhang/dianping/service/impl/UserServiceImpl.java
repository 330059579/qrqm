package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.dal.UserDAO;
import com.tuanzhang.dianping.model.User;
import com.tuanzhang.dianping.model.UserExample;
import com.tuanzhang.dianping.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;
    @Override
    public User getUser(Integer id) {
        return userDAO.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public User register(User registerUser) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        registerUser.setPassword(endoceByMd5(registerUser.getPassword()));
        registerUser.setCreateTime(new Date());
        registerUser.setUpdateTime(new Date());
        try {
            userDAO.insertSelective(registerUser);
        }catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }
        return getUser(registerUser.getId());
    }

    private String endoceByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(str.getBytes("utf-8")));
    }

    public User login(String telphone, String password) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException{
        User user = getInfo(telphone);
        if (null == user) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }

        if (!user.getPassword().equals(endoceByMd5(password))){
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }

        return user;
    }

    @Override
    public Integer countAllUser() {
        UserExample example = new UserExample();
        return userDAO.countByExample(example);
    }


    public User getInfo(String telphone){
        UserExample example = new UserExample();
        example.createCriteria().andTelphoneEqualTo(telphone);
        List<User> users = userDAO.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }

        return users.get(0);
    }
}
