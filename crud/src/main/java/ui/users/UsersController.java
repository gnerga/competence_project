package ui.users;

import ui.CLIReader;
import ui.OperationResponse;
import ui.OperationResponseResolver;

import static ui.users.UsersOperation.*;

public class UsersController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;
    private final UsersService usersService;

    public UsersController(OperationResponseResolver responseResolver, CLIReader cliReader, UsersService usersService) {
        this.responseResolver = responseResolver;
        this.cliReader = cliReader;
        this.usersService = usersService;
    }

    @Override
    public void run() {
        System.out.println("siema");
        UsersOperation userOption = selectOperation();
        handleOperation(userOption);
    }

    private void handleOperation(UsersOperation usersOperation) {
        if (usersOperation == BACK){
            //TODO cofnac jakos
        }

        responseResolver.resolve(execute(usersOperation));
    }

    private OperationResponse execute(UsersOperation operation){
        switch (operation) {
            case CREATE:
                return create();
            case READ:
                return read();
            case UPDATE:
                return update();
            case DELETE:
                return delete();
        }

        throw new IllegalArgumentException("Unsupported operation type: " + operation.name());
    }


    private OperationResponse create() {
        print("Phone number:");
        String phoneNumber = cliReader.readString();
        print("Profile");
        String profile = cliReader.readString();

        return usersService.create(phoneNumber, profile);
    }

    private OperationResponse read() {
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");
        return usersService.read(id);
    }

    private OperationResponse update() {
        return null;
    }

    private OperationResponse delete() {
        return null;
    }

    private UsersOperation selectOperation() {
        print("1. Create user");
        print("2. Read user");
        print("3. Update user");
        print("4. Delete user");
        print("");
        print("0. Back");

        int input = cliReader.readInt(integer -> integer >= 0 && integer <= 3, "That's not an integer :/", "That's not an option");
        switch (input) {
            case 1:
                return CREATE;
            case 2:
                return READ;
            case 3:
                return UPDATE;
            case 4:
                return DELETE;
            default:
                return BACK;
        }

    }

    private void print(Object object) {
        System.out.println(object);
    }
}
