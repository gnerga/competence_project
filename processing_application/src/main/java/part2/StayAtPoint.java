package part2;

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

public class StayAtPoint {

    public static void countStayAtPoint(SparkSession spark) {
        System.out.println("Count stay at point...");
        StructType schema = new StructType()
                .add("userId", "int")
                .add("poisName", "string")
                .add("enterTime", "timestamp")
                .add("exitTime", "timestamp")
                .add("duration", "string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./output/traces_duration.csv");

        Dataset<Row> rowDataset1 = dataset.withColumn("duration_in_second", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));

        Dataset<Row> dfResult = rowDataset1.groupBy("userId", "poisName")
                .sum("duration_in_second")
                .orderBy("userId", "poisName");

        dfResult.write().format("csv").save("./part1_4");
        System.out.println("Count stay at point save in part1_4");
    }
}
