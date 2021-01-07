package clustering;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

public abstract class Clustering {
    final String groupByColumn;
    final  String featureColumn;
    final  String directoryNameToSave;
    final  boolean saveModel;
    public final SparkSession spark;
    final int numberOfCentroids;
    final String description;

//    final String groupByColumn = "PoisName";
//    final  String featureColumn = "quantity";
//    final  String directoryNameToSave = "frequentUsers";
//    final  boolean saveModel = true;

    public Clustering(String groupByColumn, String featureColumn, String directoryNameToSave, boolean saveModel, int numberOfCentroids, String description, SparkSession spark) {
        this.groupByColumn = groupByColumn;
        this.featureColumn = featureColumn;
        this.directoryNameToSave = directoryNameToSave;
        this.saveModel = saveModel;
        this.numberOfCentroids = numberOfCentroids;
        this.description = description;
        this.spark = spark;
    }

    public void displayResult() {
        Dataset<Row> inputDataset = prepareData();
        KMeansModel kMeansModel = trainKMeansModel(inputDataset);
        saveKMeansModel(kMeansModel);

        Dataset<Row> predictedDataset = makeKMeansPrediction(kMeansModel, inputDataset);
        displayResult(predictedDataset, kMeansModel);
    }

    protected abstract Dataset<Row> prepareData();

    private KMeansModel trainKMeansModel(Dataset<Row> dataset){
        KMeans kmeans = new KMeans().setK(numberOfCentroids).setSeed(1L).setFeaturesCol("features")
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
        System.out.println(description);
        predictedDataset.select(groupByColumn,featureColumn,"prediction").show();

        Vector[] centers = model.clusterCenters();
        System.out.println("Cluster Centers: ");
        for (int i = 0; i < centers.length; i++) {
            System.out.println(i+". "+centers[i]);
        }

//        ClusteringEvaluator evaluator = new ClusteringEvaluator();
//        double silhouette = evaluator.evaluate(predictedDataset);
//        System.out.println("Silhouette with squared euclidean distance = " + silhouette);
    }
}
