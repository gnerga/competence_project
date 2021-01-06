package ui.users;

import domain.users.UserCreateDto;
import domain.users.UserUpdateDto;
import domain.users.UsersService;
import model.User;
import ui.common.CrudOperation;
import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.io.IntInRangeValidator;

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
        String phoneNumber = cliReader.readString((s) -> s.length() <= 12 && s.length() >= 9 && isNumeric(s), "That's not a phone number :/");
        print("Profile");
        User.Profile profile = selectProfile();

        UserCreateDto dto = new UserCreateDto(phoneNumber, profile);
        return usersService.create(dto);
    }

    private OperationResponse read() {
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");
        return usersService.read(id);
    }

    private OperationResponse update() {
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");

        OperationResponse readUser = usersService.read(id);
        if (readUser.getStatus() == OperationResponse.OperationStatus.FAILURE)
            return readUser;

        print(responseResolver.resolve(usersService.read(id)));

        print("Phone number:");
        String phoneNumber = cliReader.readString((s) -> s.length() <= 12 && s.length() >= 9 && isNumeric(s), "That's not a phone number :/");
        print("Profile");
        User.Profile profile = selectProfile();

        UserUpdateDto dto = new UserUpdateDto(id, phoneNumber, profile);
        return usersService.update(dto);
    }

    private OperationResponse delete() {
        print("User id: ");
        int id = cliReader.readInt("That's not an integer :/");

        return usersService.delete(id);
    }

    private User.Profile selectProfile() {
        print("Select user type:");
        print("1. Student");
        print("2. Teacher");
        print("3. Service staff");

        int selectedType = cliReader.readInt(new IntInRangeValidator(1, 3), "That's not an integer!");

        switch (selectedType) {
            case 1:
                return User.Profile.STUDENT;
            case 2:
                return User.Profile.TEACHER;
            case 3:
                return User.Profile.SERVICE_STAFF;
        }

        throw new IllegalStateException("Type should've been selected already!");
    }

    private CrudOperation selectOperation() {
        print("1. Create user");
        print("2. Read user");
        print("3. Update user");
        print("4. Delete user");
        print("");
        print("0. Back");

        int input = cliReader.readInt(new IntInRangeValidator(0, 4), "That's not an option");
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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
