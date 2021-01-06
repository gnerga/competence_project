package domain.csv;

import db.io.CsvExporter;
import db.io.CsvLoader;
import ui.common.OperationResponse;

public class CsvService {
    private final CsvExporter csvExporter;
    private final CsvLoader csvLoader;

    public CsvService() {
        this.csvExporter = new CsvExporter();
        this.csvLoader = new CsvLoader();
    }

    public OperationResponse exportCsv(String table) {
        String file = "export/" + table + ".csv";
        try {
            csvExporter.exportTableToCsv(file, table);
            return OperationResponse.success("Table " + table + " exported!");
        }
        catch (Exception e) {
            return OperationResponse.failure("Could not export table " + table + "!");
        }
    }

    public OperationResponse importCsv(String path, String table) {
        try {
            csvLoader.loadCsvFile(path, table);
            return OperationResponse.success("Table " + table + " imported!");
        }
        catch (Exception e) {
            return OperationResponse.failure("Could not import table " + table + "!");
        }
    }
}
