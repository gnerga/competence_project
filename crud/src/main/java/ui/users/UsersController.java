package ui.users;

import ui.OperationResponse;
import ui.OperationResponseResolver;

import static ui.users.UsersOperation.BACK;

public class UsersController implements Runnable {
    private final OperationResponseResolver responseResolver;

    public UsersController(OperationResponseResolver responseResolver) {
        this.responseResolver = responseResolver;
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
        return null;
    }

    private OperationResponse read() {
        return null;
    }

    private OperationResponse update() {
        return null;
    }

    private OperationResponse delete() {
        return null;
    }

    private UsersOperation selectOperation() {
        return null;
    }
}
