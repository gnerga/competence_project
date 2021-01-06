package domain.fakseUsers;

import model.User;

public class FakeUser {
    private final User user;
    private final String phoneNumber;

    public FakeUser(User user, String phoneNumber) {
        this.user = user;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return user.getId();
    }

    public User.Profile getProfile() {
        return user.getProfile();
    }
}
