package com.tuanzhang.dianping.dal;

import com.tuanzhang.dianping.model.Shop;
import com.tuanzhang.dianping.model.ShopExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ShopDAO {
    int countByExample(ShopExample example);

    int deleteByExample(ShopExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Shop record);

    int insertSelective(Shop record);

    List<Shop> selectByExample(ShopExample example);

    Shop selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Shop record, @Param("example") ShopExample example);

    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    List<Map<String, Object>> buildEsQuery(@Param("sellerId")Integer sellerId, @Param("categoryId")Integer categoryId, @Param("shopId")Integer shopId);
}