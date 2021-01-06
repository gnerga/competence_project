package domain.csv;

import java.util.Arrays;

public enum Tables {
    USERS("users"), HOT_SPOTS("hot_spots"), FAKE_USERS("fake_users");
    public final String option;

    Tables(String option) {
        this.option = option;
    }
}
