package db.io;

import com.opencsv.CSVWriter;
import db.QueryExecutor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter implements FileExporter {

    public CsvExporter() {

    }

    public void exportTableToCsv(String filePath, List<String[]> rows) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));

            writer.writeAll(rows, false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
