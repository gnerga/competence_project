package anonymization;

import db.ResultSetTransformer;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Pn {
    private final String phoneNumber;
    private final String fakePhoneNumber;

    public Pn(String phoneNumber, String fakePhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.fakePhoneNumber = fakePhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFakePhoneNumber() {
        return fakePhoneNumber;
    }

    @Override
    public String toString() {
        return "Pn{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", fakePhoneNumber='" + fakePhoneNumber + '\'' +
                '}';
    }

    public static class PnResultSetMapper implements ResultSetTransformer<Pn> {
        @Override
        public Pn transform(ResultSet rs) throws SQLException {
            String phoneNumber = rs.getString("phone_number");
            String fakePhoneNumber = rs.getString("fake_phone_number");

            return new Pn(phoneNumber, fakePhoneNumber);
        }
    }
}
