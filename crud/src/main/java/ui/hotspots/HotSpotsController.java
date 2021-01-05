package ui.hotspots;

import ui.common.CrudOperation;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;

import static ui.common.CrudOperation.*;

public class HotSpotsController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;

    public HotSpotsController(OperationResponseResolver responseResolver, CLIReader cliReader) {
        this.cliReader = cliReader;
        this.responseResolver = responseResolver;
    }

    @Override
    public void run() {
        CrudOperation userOption = selectOperation();
        handleOperation(userOption);
    }

    private void handleOperation(CrudOperation crudOperation) {
        if (crudOperation == BACK) {
            return;
        }

        responseResolver.resolve(execute(crudOperation));
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
        print("");
        print("Creating hotspot:");

        print("Enter hotspot name: ");


        print("Enter hotspot description: ");
        String description = cliReader.readString();

        print("Enter hotspot longitude in format XXX.XX, e.g. 124.74, -4.01: ");


        print("Enter hotspot latitude in format XXX.XX, e.g. 124.74, -4.01: ");


        print("Select hotspot type: ");

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

    private CrudOperation selectOperation() {
        print("1. Create hotspot");
        print("2. Read hotspot");
        print("3. Update hotspot");
        print("4. Delete hotspot");
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
