package ranking;

import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

public class Ranking {
    final SparkSession spark;
    private final String inputFileName;
    private final Dataset<Row> result;
    private final String path;

    public Ranking(SparkSession spark, String inputFileName, String path) {
        this.spark = spark;
        this.inputFileName = inputFileName;
        this.path = path;

        Dataset<Row> dataset = setupDataset(spark, inputFileName);
        dataset.createOrReplaceTempView("Visits");

        Dataset<Row> dataset1 = spark.sql("SELECT PoisName, count(PoisName) visit_nr from Visits group by PoisName order by visit_nr desc");

        Row first = dataset1.head();
        long highVal = (long) first.get(1);
        String high = String.valueOf(highVal);

        Row[] tail = (Row[]) dataset1.tail(1);
        long lowVal = (long) tail[0].get(1);
        String low = String.valueOf(lowVal);

        Dataset<Row> one = spark.sql("SELECT PoisName, normalize(visit_nr, " + low + ", " + high + ") normalized_visits from (SELECT PoisName, count(PoisName) visit_nr from Visits group by PoisName order by visit_nr desc) values");

        Dataset<Row> lengths = dataset.withColumn("duration_in_second", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));
        lengths = lengths.groupBy("PoisName").sum("duration_in_second").withColumnRenamed("sum(duration_in_second)", "duration_in_seconds").sort(new Column("duration_in_seconds").desc());

        first = lengths.head();
        highVal = (long) first.get(1);
        high = String.valueOf(highVal);

        tail = (Row[]) lengths.tail(1);
        lowVal = (long) tail[0].get(1);
        low = String.valueOf(lowVal);

        lengths.createOrReplaceTempView("lengths_of_stay");
        Dataset<Row> lengthsTwo = lengths.sqlContext().sql("SELECT PoisName, normalize(duration_in_seconds, " + low + ", " + high + ") lengths_normalized FROM lengths_of_stay");

        Dataset<Row> joined = one.join(lengthsTwo, "PoisName");

        joined = joined.withColumn("Rating", functions.callUDF("szafa", joined.col("normalized_visits"), joined.col("lengths_normalized")));
        joined = joined.sort(new Column("Rating").desc());

        result = joined.select("PoisName", "Rating");

        result.javaRDD().saveAsTextFile(path);
    }

    public void displayResult() {
        result.show(false);
    }

    private Dataset<Row> setupDataset(SparkSession spark, String inputFileName) {
        StructType schema = new StructType()
                .add("UserId", "int")
                .add("PoisName", "string")
                .add("EnterTime", "timestamp")
                .add("ExitTime", "timestamp")
                .add("Duration", "string");

        Dataset<Row> dataset = spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./output/" + inputFileName);
        return dataset;
    }
}
