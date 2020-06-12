package com.tuanzhang.dianping.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.Serializable;

//ALs召回算法训练
public class AlsRecallTrain implements Serializable {


    public static void main(String[] args) throws IOException {
        //初始化Spark的运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("DianPingApp").getOrCreate();
        //读取csv文件
        JavaRDD<String> csvFile = spark.read().textFile("D:\\develop\\project\\dianping\\behavior.csv").toJavaRDD();

        JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
            @Override
            public Rating call(String s) throws Exception {
                return Rating.parseRating(s);
            }
        });

        // Dataset<Row>这是Spark中最常用一个数据结构， 可以理解为数据结构中的一张表，表的列遵循Rating中的定义
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);
        //将Dataset<Row> rating中的数据80%用来做训练，20%用来做测试
        Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});

        //训练用的数据
        Dataset<Row> trainingData = splits[0];
        //测试用的数据
        Dataset<Row> testingData = splits[1];

        //setMaxIter 表示迭代次数，setRank设置的是商品特征的数量
        // setRegParam正则化系数，防止过拟合的情况，防止模型过分趋近真是数据，一旦数据有些问题，便出现很大的问题
        //过分拟合解决：增大数据规模，或者减少rank，或者增大setRegParam数值
        //欠拟合：增减rank, 减少正则化系数
        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01).setUserCol("userId").setItemCol("shopId").setRatingCol("rating");

        //启动模型训练
        ALSModel alsModel = als.fit(trainingData);
        alsModel.save("D:\\develop\\project\\alsmodel");

        //模型评测，即将test中的数据，使用我们获得的alsmodel进行预测, 这一步中alsmodel使用来testdata中的userId和productid，但并没有使用rating, 而是将预测的值作为pridiction列返回
        Dataset<Row> productions = alsModel.transform(testingData);
        //rmse 均方根误差， 预测rpridiction和真实rating的偏差的平方处于观测次数，开个根号
        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse").setLabelCol("rating").setPredictionCol("prediction");

        //输入数据
        double rmse = evaluator.evaluate(productions);
        //结果不理想，是因为数据造假，数据越多越真实，结果越准确
        //调整ALS的参数设置
        System.out.println("rmse=" + rmse);

    }


    public static class Rating implements Serializable{
        private int userId;

        private int shopId;

        private int rating;

        //str表示behavior.csv文件中的一行数据
        public static Rating parseRating(String str) {
            //替换掉每一行数据中，每一列的双引号
            str = str.replace("\"","");
            String[] split = str.split(",");
            int userId = Integer.parseInt(split[0]);
            int shopId = Integer.parseInt(split[1]);
            int rating = Integer.parseInt(split[2]);
            return new Rating(userId, shopId, rating);
        }

        public Rating(int userId, int shopId, int rating) {
            this.userId = userId;
            this.shopId = shopId;
            this.rating = rating;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }
    }
}
