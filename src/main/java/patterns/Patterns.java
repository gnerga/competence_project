package patterns;

import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

public class Patterns {

    final SparkSession spark;

    public Patterns(SparkSession spark) {
        this.spark = spark;
        String inputFileName = "log_15000_duration.csv";

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

//        dataset.createOrReplaceTempView("Visits");
//
//        dataset = dataset.where("UserId = 1").where("Day(EnterTime) = 2");
//
//        dataset = dataset.sort("UserId", "EnterTime");
//
////        Dataset<Row> traces =
////        punkt wyjscia, punkt do ktorego idziesz
////                poisName, min(enterTime) z tej samej tabeli where entertime > 2020-10-02 00:28:53
//
//        dataset.show();
    }
}
