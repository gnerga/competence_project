package domain.fakseUsers;

import db.QueryExecutor;

import java.util.Collection;
import java.util.stream.Collectors;

public class FakeUsersService {
    private final QueryExecutor executor;

    public FakeUsersService() {
        this.executor = new QueryExecutor();
    }

    public void create(Collection<FakeUser> users) {
        if (users.isEmpty()) {
            return;
        }

        String usersValues = users.stream().map(this::toSqlValues).collect(Collectors.joining(", "));
        String query = "INSERT INTO fake_users (id, phone_number, profile) VALUES " + usersValues;

        executor.insert(query);
    }

    private String toSqlValues(FakeUser user) {
        return "( " + user.getId() + ", " + string(user.getPhoneNumber()) + ", " + string(user.getProfile().getValue()) + ")";
    }

    private String string(Object object) {
        return "'" + object + "'";
    }
}
