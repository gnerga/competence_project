package ui.users;

import ui.common.CrudOperation;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;

import static ui.common.CrudOperation.*;


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
        CrudOperation crudOperation = selectOperation();
        handleOperation(crudOperation);
    }

    private void handleOperation(CrudOperation usersOperation) {
        if (usersOperation == CrudOperation.BACK) {
            return;
        }

        print(responseResolver.resolve(execute(usersOperation)));
    }

    private OperationResponse execute(CrudOperation operation) {
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
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");
        print(responseResolver.resolve(usersService.read(id)));

        print("Phone number:");
        String phoneNumber = cliReader.readString();
        print("Profile");
        String profile = cliReader.readString();

        return usersService.update(id, phoneNumber, profile);
    }

    private OperationResponse delete() {
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");

        return usersService.delete(id);
    }

    private CrudOperation selectOperation() {
        print("1. Create user");
        print("2. Read user");
        print("3. Update user");
        print("4. Delete user");
        print("");
        print("0. Back");

        int input = cliReader.readInt(integer -> integer >= 0 && integer <= 4, "That's not an integer :/", "That's not an option");
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
