// Ref: https://github.com/databricks/learning-spark/blob/master/mini-complete-example/src/main/java/com/oreilly/learningsparkexamples/mini/java/WordCount.java
// Edited by: Anas Katib
// Last updated: Aug. 23, 2017
import clustering.AverageLengthOfStayClustering;
import clustering.Clustering;
import clustering.FrequencyClustering;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import org.apache.spark.SparkConf;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import patterns.Patterns;
import ranking.Ranking;

import java.time.Duration;


public class Main {

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

        spark.sqlContext()
                .udf()
                .register( "normalize", ( Long x, Integer low, Integer high ) -> ((double) x - low) / (high - low),
                        DataTypes.DoubleType );

        spark.sqlContext()
                .udf()
                .register( "szafa", ( Double x, Double y) -> (0.6 * x) + (0.4 * y), DataTypes.DoubleType );

//        Clustering frequentUsers= getFrequencyClustering(spark);
//        frequentUsers.displayResult();
//
//        Clustering averageLengthOfStayClustering= getAverageLengthOfStayClustering(spark);
//        averageLengthOfStayClustering.displayResult();

//        Ranking ranking = new Ranking(spark);

        Patterns patterns = new Patterns(spark);
    }

    private static Clustering getAverageLengthOfStayClustering(SparkSession spark){
        final String groupByColumn = "PoisName";
        final String featureColumn = "Avg stay in seconds";
        final String directoryNameToSave = "averageLengthOfStayClustering";
        final boolean saveModel = true;
        final int numberOfCentroids = 3;
        final String inputFileName = "log_150_duration.csv";
        final String description = "Clustering points of interest by average length of stay";
        return new AverageLengthOfStayClustering(groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark);
    }

    private static Clustering getFrequencyClustering(SparkSession spark){
        final String groupByColumn = "PoisName";
        final String featureColumn = "Quantity";
        final String directoryNameToSave = "frequencyUsersClustering";
        final boolean saveModel = true;
        final int numberOfCentroids = 3;
        final String inputFileName = "log_150_duration.csv";
        final String description = "Clustering points of interest by user frequency";
    return new FrequencyClustering(groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark);
}
}



