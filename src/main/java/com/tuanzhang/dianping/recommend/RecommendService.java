package com.tuanzhang.dianping.recommend;

import com.tuanzhang.dianping.dal.RecommendDAO;
import com.tuanzhang.dianping.model.Recommend;
import com.tuanzhang.dianping.model.RecommendExample;
import org.apache.spark.sql.sources.In;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService implements Serializable {

    @Resource
    private RecommendDAO recommendDAO;

    //根据id 召回shopId的list
    public List<Integer> recall(Integer userId) {
        Recommend recommend = getRecommend(userId);
        if (null == recommend) {
            recommend = getRecommend(9999999);
        }

        String[] shopIdArr = recommend.getRecommend().split(",");
        List<Integer> shopIdList = new ArrayList<>();
        for (int i=0; i< shopIdArr.length; i++) {
            shopIdList.add(Integer.valueOf(shopIdArr[i]));
        }

        return shopIdList;
    }

    private Recommend getRecommend(Integer userId) {
        RecommendExample example = new RecommendExample();
        example.createCriteria().andIdEqualTo(userId);
        List<Recommend> recommends = recommendDAO.selectByExample(example);
        if (CollectionUtils.isEmpty(recommends)) {
            return null;
        }

        return recommends.get(0);
    }
}
