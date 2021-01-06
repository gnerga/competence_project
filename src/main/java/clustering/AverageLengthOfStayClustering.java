package clustering;

import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StructType;

public class AverageLengthOfStayClustering extends Clustering {
    private final String inputFileName;

    public AverageLengthOfStayClustering(String groupByColumn, String featureColumn, String directoryNameToSave, boolean saveModel, int numberOfCentroids, String inputFileName, String description, SparkSession spark) {
        super(groupByColumn, featureColumn, directoryNameToSave, saveModel, numberOfCentroids, description, spark);
        this.inputFileName = inputFileName;
    }

    protected Dataset<Row> prepareData() {
        // load csv file
        StructType schema = new StructType()
                .add("UserId", "int")
                .add("PoisName", "string")
                .add("EnterTime", "timestamp")
                .add("ExitTime", "timestamp")
                .add("Duration", "string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./inputfiles/" + inputFileName);

        Dataset<Row> rowDataset1 = dataset.withColumn("duration_in_second", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));

        //groupBy
        Dataset<Row> name = rowDataset1.groupBy(groupByColumn).avg("duration_in_second");

        //count quantity of elements in groups
        Dataset<Row> rowDataset = name.toDF(groupByColumn, featureColumn);

        //convert data to vector (required by spark ml)
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(new String[]{featureColumn})
                .setOutputCol("features");
        return assembler.transform(rowDataset);
    }
}
