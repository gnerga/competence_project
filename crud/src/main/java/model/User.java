package model;

public class User {
    public final int id;
    public final String phoneNumber;
    public final Profile profile;

    public User(int id, String phoneNumber, Profile profile) {
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

    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profile='" + profile.getValue() + '\'' +
                '}';
    }

    public enum Profile {
        STUDENT("Student"), TEACHER("Teacher"), SERVICE_STAFF("ServiceStaff");

        private final String value;

        private Profile(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }

        public static Profile valueOfLabel(String label) {
            for (Profile e : values()) {
                if (e.value.equals(label)) {
                    return e;
                }
            }
            return null;
        }
    }
}
