package ui.users;

import db.QueryExecutor;
import db.ResultSetTransformer;
import model.User;
import ui.OperationResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersService {
    private final QueryExecutor executor;
    public UsersService() {
        this.executor = new QueryExecutor();
    }

    public OperationResponse create(String phoneNumber, String profile) {
        String query = "";

        executor.execute(query);
        return OperationResponse.success();
    }


    public OperationResponse read(int id) {
        String query = "SELECT * " +
                "FROM users " +
                "WHERE id=" + id;

        Optional<User> result = executor.get(query, new UserTransformer());
        return OperationResponse.success();
    }

    private final static class UserTransformer implements ResultSetTransformer<User> {

        @Override
        public User transform(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String phoneNumber = rs.getString("phone_number");
            String profile = rs.getString("profile");

            return new User(id, phoneNumber, profile);
        }
    }
}
