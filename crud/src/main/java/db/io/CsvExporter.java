package db.io;

import com.opencsv.CSVWriter;
import db.QueryExecutor;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CsvExporter implements FileExporter {
    private QueryExecutor executor;

    public CsvExporter() {
        this.executor = new QueryExecutor();
    }

    public void exportTableToCsv(String filePath, String table) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));

            String loadQuery = "SELECT * FROM `" + table + "`";

            ResultSet resultSet = executor.getResultSet(loadQuery);
            writer.writeAll(resultSet, true);

            writer.close();
            resultSet.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
}
