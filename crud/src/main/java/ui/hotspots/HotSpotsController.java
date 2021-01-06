package ui.hotspots;

import db.QueryExecutor;
import domain.hotspots.HotSpotCreateDto;
import domain.hotspots.HotSpotService;
import model.HotSpot;
import model.HotSpot.Type;
import ui.common.CrudOperation;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.io.FloatInRangeValidator;
import ui.io.IntInRangeValidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static ui.common.CrudOperation.*;

public class HotSpotsController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;
    private final HotSpotService service;

    public HotSpotsController(OperationResponseResolver responseResolver, CLIReader cliReader, QueryExecutor queryExecutor) {
        this.cliReader = cliReader;
        this.responseResolver = responseResolver;
        this.service = new HotSpotService(queryExecutor);
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

        print(responseResolver.resolve(execute(crudOperation)));
    }

    private CrudOperation selectOperation() {
        print("1. Create hotspot");
        print("2. Read hotspot");
        print("3. Update hotspot");
        print("4. Delete hotspot");
        print("");
        print("0. Back");

        int input = cliReader.readInt(new IntInRangeValidator(0, 4), "That's not an integer :/");
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

        HotSpotCreateDto dto = getHotSpotCreateDto();
        return service.create(dto);
    }

    private OperationResponse read() {
        print("");
        print("Select what do you want to see:");
        print("1. All hotspots");
        print("2. Hotspot by its name");

        int selectedType = select1Or2();

        switch (selectedType) {
            case 1:
                return allHotspots();
            case 2:
                return findHotspot();
        }

        throw new IllegalStateException("No such read option!");
    }

    private OperationResponse update() {
        print("");
        String name = getHotspotName();
        Optional<HotSpot> byName = service.findByName(name);
        if (byName.isEmpty()) return OperationResponse.failure("Hotspot does not exist!");

        print("");
        print("Editing following hotspot:");
        byName.ifPresent(this::print);

        HotSpotCreateDto dto = getHotSpotCreateDto();
        return service.update(name, dto);
    }

    private OperationResponse delete() {
        print("");
        String name = getHotspotName();
        Optional<HotSpot> byName = service.findByName(name);
        if (byName.isEmpty()) return OperationResponse.failure("Hotspot does not exist!");

        print("");
        print("Removing following hotspot:");
        byName.ifPresent(this::print);

        print("Do you want to remove it?");
        print("1. No");
        print("2. Yes");

        int selected = select1Or2();

        switch (selected) {
            case 1:
                return OperationResponse.success("Hotspot not removed :)");
            case 2:
                return service.delete(name);
        }

        throw new IllegalStateException("Option should be already handled!");
    }


    private HotSpotCreateDto getHotSpotCreateDto() {
        print("Enter hotspot name: ");
        String name = cliReader.readString();

        print("Enter hotspot description: ");
        String description = cliReader.readString();

        print("Enter hotspot longitude in format XXX.XX, e.g. 124.74, -4.01: ");
        float longitude = cliReader.readFloat(new FloatInRangeValidator(-180f, 180f), "Incorrect format!");

        print("Enter hotspot latitude in format XXX.XX, e.g. 80.74, -4.01: ");
        float latitude = cliReader.readFloat(new FloatInRangeValidator(-90f, 90f), "Incorrect format!");

        Type type = selectType();

        return new HotSpotCreateDto(name, description, longitude, latitude, type);
    }

    private Type selectType() {
        print("Select hotspot type:");
        print("1. Indoor");
        print("2. Outdoor");

        int selectedType = select1Or2();

        switch (selectedType) {
            case 1:
                return Type.INDOOR;
            case 2:
                return Type.OUTDOOR;
        }

        throw new IllegalStateException("Type should've been selected already!");
    }

    private int select1Or2() {
        return cliReader.readInt(new IntInRangeValidator(1, 2), "That's not an integer!");
    }

    private OperationResponse allHotspots() {
        List<HotSpot> hotspots = service.findAll();
        hotspots.forEach(this::print);
        return hotspots.isEmpty() ? OperationResponse.failure("No hotspots found!") : OperationResponse.success();
    }

    private OperationResponse findHotspot() {
        String name = getHotspotName();
        Optional<HotSpot> byName = service.findByName(name);
        byName.ifPresent(this::print);

        return byName.isPresent() ? OperationResponse.success() : OperationResponse.failure("No hotspot found!");
    }

    private String getHotspotName() {
        print("Hotspot name:");
        return cliReader.readString();
    }

    private Predicate<Float> floatInRange(float minValue, float maxValue) {
        return (value) -> value >= minValue && value <= maxValue;
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
