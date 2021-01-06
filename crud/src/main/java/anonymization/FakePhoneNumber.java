package anonymization;

import db.ResultSetTransformer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FakePhoneNumber {
    private final int id;
    private final String fakePhoneNumber;

    public FakePhoneNumber(int id, String fakePhoneNumber) {
        this.id = id;
        this.fakePhoneNumber = fakePhoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getFakePhoneNumber() {
        return fakePhoneNumber;
    }

    @Override
    public String toString() {
        return "Pn{" +
                "id='" + id + '\'' +
                ", fakePhoneNumber='" + fakePhoneNumber + '\'' +
                '}';
    }

    public static class FakePhoneNumberResultSetMapper implements ResultSetTransformer<FakePhoneNumber> {
        @Override
        public FakePhoneNumber transform(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String fakePhoneNumber = rs.getString("fake_phone_number");

            return new FakePhoneNumber(id, fakePhoneNumber);
        }
    }
}
