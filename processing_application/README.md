### How to generate hotspot ranking:
1. Create Ranking object
``` java
Ranking ranking = new Ranking(spark, "traces_duration.csv", "./ranking_output");
```
Parameters: SparkSession, data file path, output directory path.

Remember to delete output directory before using this object!

You can also display results on console:
``` java
ranking.displayResult();
```

### How to generate most often visited destinations from hotspots pattern:
1. Create MostOftenVisitedHotspot object
``` java
MostOftenVisitedHotspot mostOftenVisitedHotspot = new MostOftenVisitedHotspot(spark, "traces_duration.csv", "./patterns_output");
```
Parameters: SparkSession, data file path, output directory path.

Remember to delete output directory before using this object!

You can also display results on console:
``` java
mostOftenVisitedHotspot.displayResult();
```

### How to clustering points of interest by frequent users:
1. Create Clustering object 
``` java
Clustering frequentUsers = getFrequencyClustering(spark);

    private static Clustering getFrequencyClustering(SparkSession spark){
        final String groupByColumn = "PoisName";
        final String featureColumn = "Quantity";
        final String directoryNameToSave = "frequencyUsersClustering";
        final boolean saveModel = true;
        final int numberOfCentroids = 3;
        final String inputFileName = "traces_duration.csv";
        final String description = "Clustering points of interest by user frequency";
    return new FrequencyClustering(groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark);
```
Parameters: groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark.

2. Run clustering
``` java
frequentUsers.displayResult();
```

Result will be display on console.

### How to clustering points of interest by length of stay:
1. Create Clustering object
``` java
Clustering averageLengthOfStayClustering = getAverageLengthOfStayClustering(spark);

    private static Clustering getAverageLengthOfStayClustering(SparkSession spark){
        final String groupByColumn = "PoisName";
        final String featureColumn = "Avg stay in seconds";
        final String directoryNameToSave = "averageLengthOfStayClustering";
        final boolean saveModel = true;
        final int numberOfCentroids = 3;
        final String inputFileName = "traces_duration.csv";
        final String description = "Clustering points of interest by average length of stay";
        return new AverageLengthOfStayClustering(groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark);
    }
```
Parameters: groupByColumn,featureColumn,directoryNameToSave,saveModel,numberOfCentroids,inputFileName, description, spark.

2. Run clustering
``` java
averageLengthOfStayClustering.displayResult();
```

Result will be display on console.


### How to calculate the length of stay at each point of interest for each user:
1. Run static method 
``` java
StayAtPoint.countStayAtPoint(spark);
```
Parameters: spark.

Remember to delete output directory before using this object!

Result will be save in directory part1_5.

### How to Calculate  the  longest  route  taken  by  each  person:
1. Run static method
``` java
LongestRoute.countRoute(spark);
```
Parameters: spark.

Remember to delete output directory before using this object!

Result will be save in directory part1_4.

