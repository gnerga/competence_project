###How to generate hotspot ranking:
1. Create Ranking object
``` java
Ranking ranking = new Ranking(spark, "log_15000_duration.csv", "./ranking_output");
```
Parameters: SparkSession, data file path, output directory path.

Remember to delete output directory before using this object!

You can also display results on console:
``` java
ranking.displayResult();
```

###How to generate most often visited destinations from hotspots pattern:
1. Create MostOftenVisitedHotspot object
``` java
ostOftenVisitedHotspot mostOftenVisitedHotspot = new MostOftenVisitedHotspot(spark, "log_15000_duration.csv", "./patterns_output");
```
Parameters: SparkSession, data file path, output directory path.

Remember to delete output directory before using this object!

You can also display results on console:
``` java
mostOftenVisitedHotspot.displayResult();
```