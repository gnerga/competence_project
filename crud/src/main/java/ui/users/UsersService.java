package ui.users;

import db.QueryExecutor;
import db.ResultSetTransformer;
import model.User;
import ui.common.OperationResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersService {
    private final QueryExecutor executor;

    public UsersService() {
        this.executor = new QueryExecutor();
    }

    public OperationResponse create(String phoneNumber, String profile) {
        String query = "INSERT INTO `users`(`phone_number`, `profile`) " +
                "VALUES (\"" + phoneNumber + "\",\"" + profile + "\")";

        executor.execute(query);
        return OperationResponse.success();
    }


    public OperationResponse read(int id) {
        String query = "SELECT * " +
                "FROM users " +
                "WHERE id=" + id;

        Optional<User> result = executor.get(query, new UserTransformer());
        return result.map(user -> OperationResponse.success(user.toString())).orElseGet(() -> OperationResponse.failure("User not found"));
    }

    public OperationResponse update(int id, String phoneNumber, String profile) {
        String query = "UPDATE `users` SET `phone_number`=\"" + phoneNumber + "\",`profile`=\"" + profile + "\" WHERE id="+id;

        executor.execute(query);
        return OperationResponse.success();
    }

    public OperationResponse delete(int id) {
        String query = "DELETE FROM `users` WHERE id="+id;

        executor.execute(query);
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
