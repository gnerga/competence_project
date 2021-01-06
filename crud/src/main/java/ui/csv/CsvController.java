package ui.csv;

import domain.csv.CsvService;
import domain.csv.Tables;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.io.IntInRangeValidator;

public class CsvController implements Runnable {
    private final CLIReader cliReader;
    private final CsvService csvService;
    private final OperationResponseResolver responseResolver;

    public CsvController(CLIReader cliReader1, OperationResponseResolver responseResolver) {
        this.cliReader = cliReader1;
        this.responseResolver = responseResolver;
        this.csvService = new CsvService();
    }

    @Override
    public void run() {
        CsvOperation operation = selectOperation();
        handleOperation(operation);
    }

    private void handleOperation(CsvOperation operation) {
        if (operation == CsvOperation.BACK) {
            return;
        }

        print(responseResolver.resolve(execute(operation)));
    }

    private OperationResponse execute(CsvOperation operation) {
        switch (operation) {
            case EXPORT:
                return exportCsv();
            case IMPORT:
                return importCsv();
        }

        throw new IllegalArgumentException("Unsupported operation type: " + operation.name());
    }

    private OperationResponse exportCsv() {
        Tables table = selectTable();
        return csvService.exportCsv(table);
    }

    private OperationResponse importCsv() {
        Tables table = selectTable();
        return csvService.importCsv(table);
    }

    private Tables selectTable() {
        print("Select table:");
        print("1. users");
        print("2. hot_spots");
        print("3. fake_users");

        int selectedTable = cliReader.readInt(new IntInRangeValidator(1, 3), "That's not an integer!");

        switch (selectedTable) {
            case 1:
                return Tables.USERS;
            case 2:
                return Tables.HOT_SPOTS;
            case 3:
                return Tables.FAKE_USERS;
        }

        throw new IllegalStateException("Table should've been selected already!");
    }

    private CsvOperation selectOperation(){
        print("1. Export");
        print("2. Import");
        print("");
        print("0. Back");

        int input = cliReader.readInt(new IntInRangeValidator(0, 2), "That's not an option");

        return CsvOperation.of(input);
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
