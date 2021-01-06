package clustering;

import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class FrequencyClustering extends Clustering{
    final String groupByColumn;
    final  String featureColumn;
    final String inputFileName;

    public FrequencyClustering(String groupByColumn, String featureColumn, String directoryNameToSave, boolean saveModel, int numberOfCentroids, String inputFileName, String description, SparkSession spark) {
        super(groupByColumn, featureColumn, directoryNameToSave, saveModel, numberOfCentroids, description, spark);
        this.groupByColumn = groupByColumn;
        this.featureColumn = featureColumn;
        this.inputFileName = inputFileName;
    }

    protected Dataset<Row> prepareData() {
        // load csv file
        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .option("inferSchema", "true")
                .option("header", "true").load("./inputfiles/" + inputFileName);
        //groupBy
        Dataset<Row> nazwa = dataset.groupBy(groupByColumn).count();

        //count quantity of elements in groups
        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, featureColumn);

        //convert data to vector (required by spark ml)
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{featureColumn})
                .setOutputCol("features");
        return assembler.transform(rowDataset);
    }
}
