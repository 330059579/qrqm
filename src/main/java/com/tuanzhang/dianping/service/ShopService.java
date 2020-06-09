package com.tuanzhang.dianping.service;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.model.Shop;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopService {


    Shop create(Shop shop) throws BusinessException;

    Shop get(Integer id);

    List<Shop> selectAll();

    Integer countAllShop();


    List<Shop> recommend(BigDecimal longitude, BigDecimal latitude);

    List<Shop> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderBy, Integer categoryId, String tags);

    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);

    Map<String, Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderBy, Integer categoryId, String tags) throws IOException;
}
