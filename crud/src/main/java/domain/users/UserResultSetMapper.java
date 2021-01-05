package domain.users;

import db.ResultSetTransformer;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultSetMapper implements ResultSetTransformer<User> {
    @Override
    public User transform(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String phoneNumber = rs.getString("phone_number");
        User.Profile profile = User.Profile.valueOfLabel(rs.getString("profile"));

        return new User(id, phoneNumber, profile);
    }
}
