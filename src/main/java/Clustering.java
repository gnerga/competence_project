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

import java.util.Collections;


public class Clustering {
    public static void main(String[] args) {

        // Create a Java Spark Context.
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
                .option("header", "true").load("./data.csv");

        dataset.show();
//        Dataset<Row> rowDataset = dataset.withColumn("nazwa",dataset.col("nazwa").cast(DataType.fromDDL("StringType"))).withColumn(
//                "features",dataset.col("features").cast(DataType.fromDDL("ArrayType(DoubleType)"))
//        );

        Dataset<Row> nazwa = dataset.groupBy("nazwa").count();

        Dataset<Row> rowDataset = nazwa.toDF("nazwa", "quantity");

        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"quantity"})
                .setOutputCol("features");

        Dataset<Row> output = assembler.transform(rowDataset);
        System.out.println("Assembled columns 'hour', 'mobile', 'userFeatures' to vector column " +
                "'features'");
        output.select("features", "nazwa").show(false);

        Dataset<Row> featuresOut = output.select("features");
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
        KMeans kmeans = new KMeans().setK(2).setSeed(1L);
        KMeansModel model = kmeans.fit(featuresOut);

// Make predictions
        Dataset<Row> predictions = model.transform(featuresOut);

// Evaluate clustering by computing Silhouette score
        ClusteringEvaluator evaluator = new ClusteringEvaluator();

        double silhouette = evaluator.evaluate(predictions);
        System.out.println("Silhouette with squared euclidean distance = " + silhouette);

// Shows the result.
        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");
        for (Vector center: centers) {
            System.out.println(center);
        }
    }
}
