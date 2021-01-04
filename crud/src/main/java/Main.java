import db.DbConnectionFactory;
import db.QueryExecutor;
import db.ResultSetTransformer;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DbConnectionFactory.initialize("jdbc:mysql://127.0.0.1:3307/GoodHotDog", "root", "root");

        String selectUsers = "SELECT * FROM users;";

        QueryExecutor executor = new QueryExecutor();
        executor.getList(selectUsers, new UserTransformer()).forEach(System.out::println);
        executor.get(selectUsers, new UserTransformer()).ifPresent(System.out::println);
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
