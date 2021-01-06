package ui.csv;

import ui.SelectedOption;

import java.util.Arrays;

public enum CsvOperation {
    EXPORT(1), IMPORT(2), BACK(0);
    private final int option;

    CsvOperation(int option) {
        this.option = option;
    }

    static CsvOperation of(int option) {
        return Arrays.stream(values()).filter(o -> o.option == option).findFirst().orElseThrow();
    }
}
