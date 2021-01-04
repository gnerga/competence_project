// Ref: https://github.com/databricks/learning-spark/blob/master/mini-complete-example/src/main/java/com/oreilly/learningsparkexamples/mini/java/WordCount.java
// Edited by: Anas Katib
// Last updated: Aug. 23, 2017
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.Iterator;


public class Main {

    public static void main(String[] args) {

        // Disable logging
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        // Create a Java Spark Context.
        SparkConf sparkConf = new SparkConf().setAppName("Hello Spark - WordCount").setMaster("local[*]");
        SparkContext sc = new SparkContext(sparkConf);

        // Load our input data.
        JavaRDD<String> lines = sc.textFile("./input.txt",2).toJavaRDD();

        // Check read data
        lines.collect().forEach(s -> System.out.println(s));

        // Split up into words.
        JavaRDD<String> words = lines.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterator<String> call(String x) {
                        return Arrays.asList(x.split(" ")).iterator();
                    }});


        // Transform into word and count.
        JavaPairRDD<String, Integer> counts = words.mapToPair(
                new PairFunction<String, String, Integer>(){
                    public Tuple2<String, Integer> call(String x){
                        return new Tuple2(x, 1);
                    }}).reduceByKey(new Function2<Integer, Integer, Integer>(){
            public Integer call(Integer x, Integer y){ return x + y;}});


        // Format the word counts:
        JavaRDD<String> countsStr = counts.map( pair -> "[" + pair._1()+"] was found "+pair._2()+" time(s)");

        // Save the word counts back out to a text file
        countsStr.saveAsTextFile("./output");
    }
}