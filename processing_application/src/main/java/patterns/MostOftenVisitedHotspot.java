package patterns;

import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.StructType;

public class MostOftenVisitedHotspot {

    final SparkSession spark;
    private final String inputFileName;
    private Dataset<Row> joined;
    private final String path;

    public MostOftenVisitedHotspot(SparkSession spark, String inputFileName, String path) {
        this.spark = spark;
        this.inputFileName = inputFileName;
        this.path = path;

        Dataset<Row> dataset = setupDataset(spark, inputFileName);

        dataset.createOrReplaceTempView("Visits");
        dataset = dataset.sort("UserId", "EnterTime");

        Dataset<Row> dataset2 = dataset;

        WindowSpec orderBy = Window.orderBy(dataset.col("UserId"));
        dataset = dataset.select(dataset.col("*"), functions.row_number().over(orderBy).$plus(1).alias("row1"));
        dataset.createOrReplaceTempView("roads");

        dataset2 = dataset2.select(dataset2.col("*"), functions.row_number().over(orderBy).alias("row2"));
        dataset2.createOrReplaceTempView("connected");

        joined = spark.sql("SELECT roads.UserId, roads.PoisName Start, roads.EnterTime, connected.PoisName Destination FROM roads INNER JOIN connected on roads.row1 = connected.row2 AND roads.UserId = connected.UserId");
        joined.createOrReplaceTempView("joined");

        joined = joined.sqlContext().sql("SELECT concat(Start, ' - ', Destination) Road, Start, Destination, count(UserId) number_of_travels FROM joined GROUP BY Road, Start, Destination");
        joined.createOrReplaceTempView("joined");

        joined = joined.sqlContext().sql("SELECT out.Start, out.Destination, inn.most_visited FROM (SELECT Start, MAX(number_of_travels) as most_visited FROM joined GROUP BY Start) inn INNER JOIN joined out ON inn.Start = out.Start AND inn.most_visited = out.number_of_travels");

        joined.javaRDD().saveAsTextFile(path);
    }

    public void displayResult() {
        joined.show(false);
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
                .option("header", "true").load("./inputfiles/" + inputFileName);
        return dataset;
    }
}
