package ui;

import db.QueryExecutor;
import domain.users.UsersService;
import ui.common.OperationResponseResolver;
import ui.hotspots.HotSpotsController;
import ui.io.CLIReader;
import ui.io.IntInRangeValidator;
import ui.users.UsersController;

public class CommandLineInterface implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;
    private final QueryExecutor queryExecutor;

    public CommandLineInterface(OperationResponseResolver responseResolver, CLIReader cliReader) {
        this.responseResolver = responseResolver;
        this.cliReader = cliReader;
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
                case EXIT:
                    return;
            }
            print("\n\n\n");
        }
    }

    private SelectedOption chooseAction() {
        print("1. Manage users");
        print("2. Manage hotspots");
        print("");
        print("0. Exit");

        int input = cliReader.readInt(new IntInRangeValidator(0, 2), "That's not an integer :/");
        return SelectedOption.of(input);
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
