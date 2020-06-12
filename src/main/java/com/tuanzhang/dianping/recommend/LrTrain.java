package com.tuanzhang.dianping.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class LrTrain {


    public static void main(String[] args) {
        //初始化Spark的运行环境
        SparkSession spark = SparkSession.builder().master("local").appName("DianPingApp").getOrCreate();
        //读取csv文件
        JavaRDD<String> csvFile = spark.read().textFile("D:\\develop\\project\\dianping\\feature.csv").toJavaRDD();

        //做转化
        JavaRDD<Row> rowJavaRDD = csvFile.map(new Function<String, Row>() {
            @Override
            public Row call(String s) throws Exception {
                s = s.replace("\"", "");
                String[] strArr = s.split(",");
                //第一个参数表示最后的结果， 即用户的点击为0还是1的，在第12列, 第二个参数表示前11列数据构成的一个11纬向量
                return RowFactory.create(new Double(strArr[11]), Vectors.dense(Double.valueOf(strArr[0]),Double.valueOf(strArr[1]),
                        Double.valueOf(strArr[2]),Double.valueOf(strArr[3]), Double.valueOf(strArr[4]),Double.valueOf(strArr[5]),Double.valueOf(strArr[6]),
                        Double.valueOf(strArr[7]), Double.valueOf(strArr[8]),Double.valueOf(strArr[9]),Double.valueOf(strArr[10])));
            }
        });

        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.DoubleType, false, Metadata.empty())
                new StructField("features", new VectorUDT(), false, Metadata.empty())
        });


        Dataset<Row> data = spark.createDataFrame(rowJavaRDD, schema);
        //分开训练和测试数据
        Dataset<Row>[] splits = data.randomSplit(new double[]{0.8, 0.2});

        //训练用的数据
        Dataset<Row> trainingData = splits[0];
        //测试用的数据
        Dataset<Row> testingData = splits[1];

        LogisticRegression lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8).setFamily("multinomial");
        //获得模型
        LogisticRegressionModel lrModel = lr.fit(trainingData);
        lrModel.save("D:\\develop\\project\\lrmodel");
    }

}
