package calculations;

import clustering.model.PoiDto;
import clustering.model.Trace;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Calculations {
    private final static String INPUT_FILE_NAME = "log_15000_duration.csv";

    private static Dataset<Row> prepareDataset(SparkSession spark) {
        StructType schema = new StructType()
                .add("userId", "int")
                .add("poisName", "string")
                .add("enterTime", "timestamp")
                .add("exitTime", "timestamp")
                .add("duration", "string");

        return spark.read().format("csv")
                .option("sep", ",")
                .schema(schema)
                .option("header", "true").load("./inputfiles/" + INPUT_FILE_NAME);
    }

    public static void countStayAtPoisForUsers(SparkSession spark) {
        System.out.println("1.4 Count stay at pois for users");

        Dataset<Row> dataset = prepareDataset(spark);

        Dataset<Row> dsWithDurationInSec = dataset.withColumn("duration_in_second", functions.callUDF("sampleUDFLambda", dataset.col("Duration")));

        Dataset<Row> dfResult = dsWithDurationInSec.groupBy("userId", "poisName")
                .sum("duration_in_second")
                .orderBy("userId", "poisName");

        dfResult.write().format("csv").save("./part1_4");
        System.out.println("Count stay at pois saved in directory part1_4");
    }

    public static void countLongestRoute(SparkSession spark) {
        System.out.println("1.5 Count longest route for each person");

        Dataset<Row> dataset = prepareDataset(spark);

        Dataset<Trace> as = dataset.as(Encoders.bean(Trace.class));

        JavaRDD<Trace> traceJavaRDD = as.javaRDD();


        JavaPairRDD<Integer, PoiDto> integerAJavaPairRDD = traceJavaRDD
                .mapToPair((PairFunction<Trace, Integer, PoiDto>) trace -> new Tuple2<>(trace.getUserId(), new PoiDto(trace.getPoisName(), trace.getEnterTime())))
                .reduceByKey((Function2<PoiDto, PoiDto, PoiDto>) (x, y) -> {
                    x.addTime(y);
                    return x;
                });

        JavaRDD<String> map1 = integerAJavaPairRDD.map(e -> {
            Map<Long, String> test = e._2().getTest();
            SortedSet<Long> keys = new TreeSet<>(test.keySet());
            List<String> visitedPoints = new ArrayList<>();
            for (Long key : keys) {
                visitedPoints.add(test.get(key));
            }
            int i = CalculationsHelperGFG.longestUniqueSubsttr(visitedPoints);
            return e._1 + "  " + i;
        });

        map1.saveAsTextFile("./part1_5");
        System.out.println("Result of longest route saved in directory: part1_5");
    }
}
