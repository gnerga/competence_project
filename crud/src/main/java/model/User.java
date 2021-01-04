package model;

public class User {
    public final int id;
    public final String phoneNumber;
    public final String profile;

    public User(int id, String phoneNumber, String profile) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
