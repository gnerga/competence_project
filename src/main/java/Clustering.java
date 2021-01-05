import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;
import java.util.Collections;


public class Clustering {
    final static String groupByColumn = "PoisName";

    public static void main(String[] args) {

        // Disable logging
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        SparkConf sparkConf = new SparkConf().setAppName("Hello Spark - WordCount").setMaster("local[*]");

        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config(sparkConf)
                .getOrCreate();
        // Loads data.

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .option("inferSchema", "true")
                .option("header", "true").load("./trace_duration.csv");

        dataset.show();
//        Dataset<Row> rowDataset = dataset.withColumn("nazwa",dataset.col("nazwa").cast(DataType.fromDDL("StringType"))).withColumn(
//                "features",dataset.col("features").cast(DataType.fromDDL("ArrayType(DoubleType)"))
//        );

        Dataset<Row> nazwa = dataset.groupBy(groupByColumn).count();

        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, "quantity");

        rowDataset.show();

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"quantity"})
                .setOutputCol("features");

        Dataset<Row> output = assembler.transform(rowDataset);
        System.out.println("Assembled columns 'hour', 'mobile', 'userFeatures' to vector column " +
                "'features'");
        output.select("features", groupByColumn).show(false);

//
//        rowDataset.collect()

//        rowDataset
//
//        rowDataset.foreach();
//
//        rowDataset.foreach();
//
//        rowDataset.foreach();
//        Vectors.dense()

// Trains a k-means model.
        KMeans kmeans = new KMeans().setK(3).setSeed(1L).setFeaturesCol("features")
                .setPredictionCol("prediction");
        KMeansModel model = kmeans.fit(output);
        try {
            model.write().overwrite().save("./k_means_model/frequentUsers");
        } catch (IOException ignored) {}

// Make predictions
        Dataset<Row> predictions = model.transform(output);
        predictions.show();

// Evaluate clustering by computing Silhouette score
        ClusteringEvaluator evaluator = new ClusteringEvaluator();

        double silhouette = evaluator.evaluate(predictions);
        System.out.println("Silhouette with squared euclidean distance = " + silhouette);

// Shows the result.
        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");
        for (int i = 0; i < centers.length; i++) {
            System.out.println(i+". "+centers[i]);
        }
    }
}
