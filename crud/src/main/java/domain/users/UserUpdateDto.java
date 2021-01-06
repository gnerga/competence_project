package domain.users;

import model.User;

public class UserUpdateDto {
    public final int id;
    public final String phoneNumber;
    public final User.Profile profile;

    public UserUpdateDto(int id, String phoneNumber, User.Profile profile) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }
}
