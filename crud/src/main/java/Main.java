import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3307/GoodHotDog";
        String user = "root";
        String password = "root";

        String selectUsers = "SELECT * FROM users;";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet users = statement.executeQuery(selectUsers);

            while(users.next()){
                int id = users.getInt("id");
                String phoneNumber = users.getString("phone_number");
                String profile = users.getString("profile");

                System.out.println("User{id: " + id + ", phoneNumber: " + phoneNumber + ", profile: " + profile);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
