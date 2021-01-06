package ui;

import java.util.Arrays;

public enum SelectedOption {
    MANAGE_USERS(1), MANAGE_HOTSPOTS(2), EXPORT_IMPORT_CSV(3), EXIT(0);

    private final int option;

    SelectedOption(int option) {
        this.option = option;
    }

    static SelectedOption of(int option) {
        return Arrays.stream(values()).filter(o -> o.option == option).findFirst().orElseThrow();
    }
}
