package com.tuanzhang.dianping.service;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.model.Shop;

import java.util.List;

public interface ShopService {


    Shop create(Shop shop) throws BusinessException;

    Shop get(Integer id);

    List<Shop> selectAll();

    Integer countAllShop();
}
