package ui;

import config.PropertiesLoader;
import db.QueryExecutor;
import domain.users.UsersService;
import ui.common.OperationResponseResolver;
import ui.csv.CsvController;
import ui.hotspots.HotSpotsController;
import ui.io.CLIReader;
import ui.io.IntInRangeValidator;
import ui.traces.TracesController;
import ui.traces.TracesGenerationConfiguration;
import ui.users.UsersController;

public class CommandLineInterface implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;
    private PropertiesLoader propertiesLoader;
    private final QueryExecutor queryExecutor;

    public CommandLineInterface(OperationResponseResolver responseResolver, CLIReader cliReader, PropertiesLoader loader) {
        this.responseResolver = responseResolver;
        this.cliReader = cliReader;
        this.propertiesLoader = loader;
        this.queryExecutor = new QueryExecutor();
    }

    @Override
    public void run() {
        while (true) {
            SelectedOption selectedOption = chooseAction();
            switch (selectedOption) {
                case MANAGE_USERS:
                    new UsersController(responseResolver, cliReader, new UsersService()).run();
                    break;
                case MANAGE_HOTSPOTS:
                    new HotSpotsController(responseResolver, cliReader, queryExecutor).run();
                    break;
                case EXPORT_IMPORT_CSV:
                    new CsvController(cliReader, responseResolver).run();
                    break;
                case GENERATE_TRACES:
                    new TracesController(cliReader, responseResolver, new TracesGenerationConfiguration(propertiesLoader)).run();
                    break;
                case EXIT:
                    return;
            }

            print("\n\n\n");
        }
    }

    private SelectedOption chooseAction() {
        print("1. Manage users");
        print("2. Manage hotspots");
        print("3. Export/import CSV file");
        print("4. Generate traces");
        print("");
        print("0. Exit");

        int input = cliReader.readInt(new IntInRangeValidator(0, 4), "That's not an integer :/");
        return SelectedOption.of(input);
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
