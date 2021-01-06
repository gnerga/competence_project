package db.io;

import db.QueryExecutor;

public class CsvLoader implements FileExporter{
    private QueryExecutor executor;

    public CsvLoader() {
        this.executor = new QueryExecutor();
    }

    public void loadCsvFile(String filePath, String table){
        String loadQuery = "LOAD DATA INFILE '" + filePath + "' INTO TABLE " + table + "\n" +
                "FIELDS TERMINATED BY ',' ENCLOSED BY '\\\"'\n" +
                "LINES TERMINATED BY '" + System.lineSeparator() + "'";

        executor.execute(loadQuery);
    }
}
