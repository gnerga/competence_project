package ui;

import ui.users.UsersController;

public class CommandLineInterface implements Runnable {
    private final OperationResponseResolver responseResolver;

    public CommandLineInterface(OperationResponseResolver responseResolver) {
        this.responseResolver = responseResolver;
    }

    @Override
    public void run() {
        SelectedOption selectedOption = chooseAction();

        switch (selectedOption) {
            case MANAGE_USERS:
                new UsersController(responseResolver).run();
            case MANAGE_HOTSPOTS:
                break;
        }

    }

    private SelectedOption chooseAction() {
        return null;
    }
}
