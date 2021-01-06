package domain.users;

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

    public OperationResponse create(UserCreateDto dto) {
        String query = "INSERT INTO `users`(`phone_number`, `profile`) " +
                "VALUES (\"" + dto.phoneNumber + "\",\"" + dto.profile.getValue() + "\")";

        try {
            executor.insert(query);
            return OperationResponse.success("User created!");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResponse.failure("Could not create this user!");
        }
    }

    public OperationResponse read(int id) {
        String query = "SELECT * " +
                "FROM users " +
                "WHERE id=" + id;

        Optional<User> result = executor.get(query, new UserResultSetMapper());
        return result.map(user -> OperationResponse.success(user.toString())).orElseGet(() -> OperationResponse.failure("User not found"));
    }

    public OperationResponse update(UserUpdateDto dto) {
        String query = "UPDATE `users` SET `phone_number`=\"" + dto.phoneNumber + "\",`profile`=\"" + dto.profile.getValue() + "\" WHERE id="+dto.id;

        try {
            executor.execute(query);
            return OperationResponse.success("User updated!");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResponse.failure("Could not update this user!");
        }
    }

    public OperationResponse delete(int id) {
        String query = "DELETE FROM `users` WHERE id="+id;

        try {
            executor.execute(query);
            return OperationResponse.success("User deleted!");
        } catch (Exception e) {
            e.printStackTrace();
            return OperationResponse.failure("Could not delete this user!");
        }
    }
}
