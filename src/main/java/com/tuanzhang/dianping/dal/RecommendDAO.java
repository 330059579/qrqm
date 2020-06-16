package com.tuanzhang.dianping.dal;

import com.tuanzhang.dianping.model.Recommend;
import com.tuanzhang.dianping.model.RecommendExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecommendDAO {
    int countByExample(RecommendExample example);

    int deleteByExample(RecommendExample example);

    int insert(Recommend record);

    int insertSelective(Recommend record);

    List<Recommend> selectByExample(RecommendExample example);

    int updateByExampleSelective(@Param("record") Recommend record, @Param("example") RecommendExample example);

    int updateByExample(@Param("record") Recommend record, @Param("example") RecommendExample example);
}