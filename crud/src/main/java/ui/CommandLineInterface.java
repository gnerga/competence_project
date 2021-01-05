package ui;

import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.users.UsersController;
import ui.users.UsersService;

public class CommandLineInterface implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;

    public CommandLineInterface(OperationResponseResolver responseResolver, CLIReader cliReader) {
        this.responseResolver = responseResolver;
        this.cliReader = cliReader;
    }

    @Override
    public void run() {
        SelectedOption selectedOption = chooseAction();

        switch (selectedOption) {
            case MANAGE_USERS:
                new UsersController(responseResolver, cliReader, new UsersService()).run();
            case MANAGE_HOTSPOTS:
                break;
        }
    }

    private SelectedOption chooseAction() {
        print("1. Manage users");
        print("2. Manage hotspots");
        print("");
        print("0. Exit");

        int input = cliReader.readInt(integer -> integer >= 0 && integer <= 3, "That's not an integer :/", "That's not an option");
        return SelectedOption.of(input);
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
