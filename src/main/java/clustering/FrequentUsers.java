package clustering;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

public class FrequentUsers {
    final static String groupByColumn = "PoisName";
    final static String featureColumn = "quantity";
    final static String directoryNameToSave = "frequentUsers";
    final static boolean saveModel = true;
    SparkSession spark;

    public FrequentUsers(SparkSession spark) {
        this.spark = spark;
    }

    public void displayResult() {
        Dataset<Row> inputDataset = prepareData2();
        KMeansModel kMeansModel = trainKMeansModel(inputDataset);
        saveKMeansModel(kMeansModel);

        Dataset<Row> predictedDataset =  makeKMeansPrediction(kMeansModel, inputDataset);
        displayResult(predictedDataset, kMeansModel);
    }

    private Dataset<Row> prepareData2() {
        // load csv file
        StructType schema = new StructType()
                .add("UserId","int")
                .add("PoisName","string")
                .add("EnterTime","timestamp")
                .add("ExitTime","timestamp")
                .add("Duration","string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./trace_duration.csv");

        Dataset<Row> rowDataset1 = dataset.withColumn("e", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));

        rowDataset1.show();

        Dataset<Row> dataset2 = dataset.withColumn("test",dataset.col("EnterTime").cast("int"));

        //groupBy
        Dataset<Row> nazwa = dataset2.groupBy(groupByColumn).sum("test");

        //count quantity of elements in groups
        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, "quantity");

        //convert data to vector (required by spark ml)
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"quantity"})
                .setOutputCol("features");
        return assembler.transform(rowDataset);
    }

    private Dataset<Row> prepareData3() {
        // load csv file
        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .option("inferSchema", "true")
                .option("header", "true").load("./trace_duration.csv");
        dataset.show();
        //groupBy
        Dataset<Row> nazwa = dataset.groupBy(groupByColumn).count();

        //count quantity of elements in groups
        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, "quantity");

        //convert data to vector (required by spark ml)
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"quantity"})
                .setOutputCol("features");
        return assembler.transform(rowDataset);
    }

    private Dataset<Row> prepareData() {
        // load csv file
        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .option("inferSchema", "true")
                .option("header", "true").load("./trace_duration.csv");
        dataset.show();
        //groupBy
        Dataset<Row> nazwa = dataset.groupBy(groupByColumn).count();

        //count quantity of elements in groups
        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, "quantity");

        //convert data to vector (required by spark ml)
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{"quantity"})
                .setOutputCol("features");
        return assembler.transform(rowDataset);
    }

    private KMeansModel trainKMeansModel(Dataset<Row> dataset){
        KMeans kmeans = new KMeans().setK(2).setSeed(1L).setFeaturesCol("features")
                .setPredictionCol("prediction");
        return kmeans.fit(dataset);
    }

    private void saveKMeansModel(KMeansModel model){
        if (saveModel) {
            String filePath = "./k_means_model/" + directoryNameToSave;
            try {
                model.write().overwrite().save(filePath);
            } catch (IOException ignored) {
                System.out.println("Save k-menas model to "+ filePath + " failed. :(");
            }
        }
    }

    private Dataset<Row> makeKMeansPrediction(KMeansModel model, Dataset<Row> dataset){
        return model.transform(dataset);
    }

    private void displayResult(Dataset<Row> predictedDataset, KMeansModel model) {
        predictedDataset.select(groupByColumn,featureColumn,"prediction").show();

        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");
        for (int i = 0; i < centers.length; i++) {
            System.out.println(i+". "+centers[i]);
        }

        ClusteringEvaluator evaluator = new ClusteringEvaluator();
        double silhouette = evaluator.evaluate(predictedDataset);
        System.out.println("Silhouette with squared euclidean distance = " + silhouette);
    }
}
