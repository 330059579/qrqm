package com.tuanzhang.dianping.recommend;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class AlsRecallPredict {


    public static void main(String[] args) {
        //初始化Spark的运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("DianPingApp").getOrCreate();
        //加载训练模型进内存
        ALSModel alsModel = ALSModel.load("D:\\develop\\project\\alsmodel");

        //给5个用户做离线召回结果预测
        //读取csv文件
        JavaRDD<String> csvFile = spark.read().textFile("D:\\develop\\project\\dianping\\behavior.csv").toJavaRDD();

        JavaRDD<AlsRecallTrain.Rating> ratingJavaRDD = csvFile.map(new Function<String, AlsRecallTrain.Rating>() {
            @Override
            public AlsRecallTrain.Rating call(String s) throws Exception {
                return AlsRecallTrain.Rating.parseRating(s);
            }
        });

        // Dataset<Row>这是Spark中最常用一个数据结构， 可以理解为数据结构中的一张表，表的列遵循Rating中的定义
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, AlsRecallTrain.Rating.class);
        //拿到了5个用户行
        Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct().limit(5);
        Dataset<Row> userRecs = alsModel.recommendForUserSubset(users, 20);

        userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {

            @Override
            public void call(Iterator<Row> iterator) throws Exception {

                Connection connection = DriverManager.getConnection("jdbc:mysql://106.52.236.197:3306/dianping?user=root&password=123456&" +
                        "useUnicode=true&characterEncoding=UTF-8");
                PreparedStatement preparedStatement = connection.prepareStatement("insert into recommend(id, recommend)values(?,?)");
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                iterator.forEachRemaining(action -> {
                    int userId = action.getInt(0);
                    List<GenericRowWithSchema> recommondationList = action.getList(1);
                    List<Integer> shopIdList = new ArrayList<>();
                    recommondationList.forEach(row ->{
                         Integer shopId = row.getInt(0);
                        shopIdList.add(shopId);
                    });
                    String recommendData = StringUtils.join(shopIdList, ",");
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", userId);
                    map.put("recommend", recommendData);
                    data.add(map);
                    System.out.println(shopIdList);
                });

                data.forEach(map ->{
                    try {
                        preparedStatement.setInt(1, (Integer) map.get("userId"));
                        preparedStatement.setString(2, (String) map.get("recommend"));
                        preparedStatement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace(); } });
                preparedStatement.executeBatch();
                preparedStatement.close();
            }
        });

    }


    public static class Rating implements Serializable {
        private int userId;

        private int shopId;

        private int rating;

        //str表示behavior.csv文件中的一行数据
        public static AlsRecallTrain.Rating parseRating(String str) {
            //替换掉每一行数据中，每一列的双引号
            str = str.replace("\"","");
            String[] split = str.split(",");
            int userId = Integer.parseInt(split[0]);
            int shopId = Integer.parseInt(split[1]);
            int rating = Integer.parseInt(split[2]);
            return new AlsRecallTrain.Rating(userId, shopId, rating);
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
