package ui.hotspots;

import model.HotSpot.Type;
import ui.common.CrudOperation;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;

import java.util.function.Predicate;

import static ui.common.CrudOperation.*;

public class HotSpotsController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;
    private final HotSpotService service;

    public HotSpotsController(OperationResponseResolver responseResolver, CLIReader cliReader, HotSpotService service) {
        this.cliReader = cliReader;
        this.responseResolver = responseResolver;
        this.service = service;
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
        String name = cliReader.readString();

        print("Enter hotspot description: ");
        String description = cliReader.readString();

        print("Enter hotspot longitude in format XXX.XX, e.g. 124.74, -4.01: ");
        float longitude = cliReader.readFloat(floatInRange(-180f, 180f), "Value must be in range [-180; 180]", "Incorrect format!");

        print("Enter hotspot latitude in format XXX.XX, e.g. 80.74, -4.01: ");
        float latitude = cliReader.readFloat(floatInRange(-90.0f, 90f), "Value must be in range [-90; 90]", "Incorrect format!");

        print("Select hotspot type: ");
        Type type = selectType();

        var dto = new HotSpotCreateDto(name, description, longitude, latitude, type);
        return service.create(dto);
    }

    private Predicate<Float> floatInRange(float minValue, float maxValue) {
        return (value) -> value >= minValue && value <= maxValue;
    }

    private Type selectType() {
        print("Select hotspot type:");
        print("1. Indoor");
        print("2. Outdoor");

        int selectedType = cliReader.readInt((i) -> i == 1 || i == 2, "Select 1 or 2!", "That's not an integer!");

        switch (selectedType) {
            case 1:
                return Type.INDOOR;
            case 2:
                return Type.OUTDOOR;
        }

        throw new IllegalStateException("Type should've been selected already!");
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
