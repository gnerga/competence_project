package domain.csv;

import config.InputFilesConfigResolver;
import config.PropertiesLoader;
import db.QueryExecutor;
import db.ResultSetTransformer;
import db.io.CsvExporter;
import db.io.CsvLoader;
import ui.common.OperationResponse;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvService {
    private final CsvExporter csvExporter;
    private final CsvLoader csvLoader;
    private final QueryExecutor executor;

    public CsvService() {
        this.csvExporter = new CsvExporter();
        this.csvLoader = new CsvLoader();
        this.executor = new QueryExecutor();
    }

    public OperationResponse exportCsv(Tables table) {
        String path = "../output/" + table.option + ".csv";

        String loadQuery = "SELECT * FROM `" + table.option + "`";

        List<String[]> rows = executor.getList(loadQuery, new ResultSetMapper());

        try {
            csvExporter.exportTableToCsv(path, rows);
            return OperationResponse.success("Table " + table + " exported!");
        }
        catch (Exception e) {
            return OperationResponse.failure("Could not export table " + table + "!");
        }
    }

    public OperationResponse importCsv(Tables table) {
        try {
            var inputResolver = new InputFilesConfigResolver(new PropertiesLoader());
            File file = new File(inputResolver.getImportPath() + table.option + ".csv");
            csvLoader.loadCsvFile(file.getAbsolutePath(), table.option);
            return OperationResponse.success("Table " + table + " imported!");
        }
        catch (Exception e) {
            return OperationResponse.failure("Could not import table " + table + "!");
        }
    }

    public class ResultSetMapper implements ResultSetTransformer<String[]>{

        @Override
        public String[] transform(ResultSet rs) throws SQLException {
            int size = rs.getMetaData().getColumnCount();
            List<String> result = new ArrayList<>();
            for (int i = 1; i <= size; i++){
                result.add(rs.getString(i));
            }

            return result.toArray(result.toArray(new String[0]));
        }
    }
}
