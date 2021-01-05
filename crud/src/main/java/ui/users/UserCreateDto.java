package ui.users;

import model.User;

public class UserCreateDto {
    public final String phoneNumber;
    public final User.Profile profile;

    public UserCreateDto(String phoneNumber, User.Profile profile) {
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }
}
