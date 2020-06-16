package com.tuanzhang.dianping.recommend;

import com.tuanzhang.dianping.model.Shop;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;
import shapeless.ops.tuple;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RecommendSortService {

    private SparkSession spark;

    private LogisticRegressionModel lrModel;

    @PostConstruct
    public void init(){
         spark = SparkSession.builder().master("local").appName("DianPingApp").getOrCreate();
         //加载lr进内存
         lrModel = LogisticRegressionModel.load("D:\\develop\\project\\lrmodel");
    }


    public List<Integer> sort(List<Integer> shopIdList, Integer userId) {
        List<ShopSortModel> list = new ArrayList<>();
        //根据lr model所需要的11维x,生成特征，然后条用器预测方法
        for (Integer shopId :shopIdList) {
            //这里用假数据，真实请款下需要去到这个用户的信息，然后计算出11纬度的x
            Vector v = Vectors.dense(1,0,0,0,0,1,0.6,0,0,1,0);
            Vector result = lrModel.predictProbability(v);
            //数组中有两个值第一个是正样本，第二个是负样本，两个相永远为1
            double[] arr = result.toArray();
            double source = arr[1];
            ShopSortModel shopSortModel = new ShopSortModel();
            shopSortModel.setShopId(shopId);
            shopSortModel.setScore(source);
            list.add(shopSortModel);
        }

        list.sort(new Comparator<ShopSortModel>() {
            @Override
            public int compare(ShopSortModel o1, ShopSortModel o2) {
                if (o1.getScore() < o2.getScore()){
                    return 1;
                }else if (o1.getScore() > o2.getScore()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        return list.stream().map(ShopSortModel::getShopId).collect(Collectors.toList());
    }
}
