package ui;

import java.util.Arrays;

public enum SelectedOption {
    MANAGE_USERS(1), MANAGE_HOTSPOTS(2), EXIT(0);

    SelectedOption(int option) {
        this.option = option;
    }

    private final int option;

    static SelectedOption of(int option) {
        return Arrays.stream(values()).filter(o -> o.option == option).findFirst().orElseThrow();
    }
}
