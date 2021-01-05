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

        executor.insert(query);
        return OperationResponse.success();
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

        executor.execute(query);
        return OperationResponse.success();
    }

    public OperationResponse delete(int id) {
        String query = "DELETE FROM `users` WHERE id="+id;

        executor.execute(query);
        return OperationResponse.success();
    }
}
