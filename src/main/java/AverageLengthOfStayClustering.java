import clustering.Clustering;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.time.Duration;

public class AverageLengthOfStayClustering {

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
                .register("sampleUDFLambda", (String s1) -> {
                    Duration pt8H30M = Duration.parse(s1);
                    return pt8H30M.getSeconds();
                }, DataTypes.LongType);

        // load csv file
        StructType schema = new StructType()
                .add("userId", "int")
                .add("poisName", "string")
                .add("enterTime", "timestamp")
                .add("exitTime", "timestamp")
                .add("duration", "string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./inputfiles/log_15000_duration.csv");

        Dataset<Row> rowDataset1 = dataset.withColumn("duration_in_second", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));

        //groupBy
        Dataset<Row> nazwa = rowDataset1.groupBy("UserId", "PoisName").sum("duration_in_second");

        nazwa.show();
    }

//        //count quantity of elements in groups
//        Dataset<Row> rowDataset = nazwa.toDF(groupByColumn, featureColumn);
//
//        //convert data to vector (required by spark ml)
//        VectorAssembler assembler = new VectorAssembler()
//                .setInputCols(new String[]{featureColumn})
//                .setOutputCol("features");
//        return assembler.transform(rowDataset);
//    }
}
