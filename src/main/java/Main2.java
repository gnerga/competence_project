// Ref: https://github.com/databricks/learning-spark/blob/master/mini-complete-example/src/main/java/com/oreilly/learningsparkexamples/mini/java/WordCount.java
// Edited by: Anas Katib
// Last updated: Aug. 23, 2017

import clustering.*;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.time.Duration;
import java.util.*;


public class Main2 {

    public static void main(String[] args) {
        // Disable logging
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        SparkConf sparkConf = new SparkConf().setAppName("Competence Project 2021").setMaster("local[*]");

        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config(sparkConf)
                .getOrCreate();

        spark.sqlContext()
                .udf()
                .register( "sampleUDFLambda", ( String s1 ) -> {
                    Duration pt8H30M = Duration.parse(s1);
                    return pt8H30M.getSeconds();
                }, DataTypes.LongType );

        // load csv file
        StructType schema = new StructType()
                .add("userId","int")
                .add("poisName","string")
                .add("enterTime","timestamp")
                .add("exitTime","timestamp")
                .add("duration","string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./inputfiles/log_15000_duration.csv");

        Dataset<Trace> as = dataset.as(Encoders.bean(Trace.class));
        as.show();

        JavaRDD<Trace> traceJavaRDD = as.javaRDD();


        JavaPairRDD<Integer, A> integerAJavaPairRDD = traceJavaRDD.mapToPair(new PairFunction<Trace, Integer, A>() {
            @Override
            public Tuple2<Integer, A> call(Trace trace) throws Exception {
                return new Tuple2<Integer, A>(trace.getUserId(), new A(trace.getPoisName(), trace.getEnterTime()));
            }
        }).reduceByKey(new Function2<A, A, A>(){
            public A call(A x, A y){ x.addTime(y);
            return  x;}});

        JavaRDD<String> map1 = integerAJavaPairRDD.map(e -> {
            Map<Long, String> test = e._2().getTest();
            SortedSet<Long> keys = new TreeSet<>(test.keySet());
            List<String> visitedPoints = new ArrayList<>();
            for (Long key : keys) {
                visitedPoints.add(test.get(key));
            }
            int i = GFG.longestUniqueSubsttr(visitedPoints);
            return e._1 + "  " + i;
        });

        map1.saveAsTextFile("./test4");

        JavaRDD<String> map = integerAJavaPairRDD.map(pair -> pair._1() + " " + pair._2().toString());
//        String s = map.toString();
//        System.out.println(s);
        map.saveAsTextFile("./test3");


//                reduceByKey(new Function2<A, A, String>(){
//            @Override
//            public String call(A a, A a2) throws Exception {
//                return null;
//            }
    }
}



