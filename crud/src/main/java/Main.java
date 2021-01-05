import config.DbConfigResolver;
import config.PropertiesLoader;
import db.DbConfiguration;
import db.DbConnectionFactory;
import db.QueryExecutor;
import db.ResultSetTransformer;
import db.io.CsvExporter;
import db.io.CsvLoader;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        initializeDbConnectionFactory();

        String selectUsers = "SELECT * FROM users;";

        QueryExecutor executor = new QueryExecutor();
        executor.getList(selectUsers, new UserTransformer()).forEach(System.out::println);
       // executor.get(selectUsers, new UserTransformer()).ifPresent(System.out::println);

        CsvLoader loader = new CsvLoader();
        loader.loadCsvFile("/home/stanislawr/Pulpit/users.csv", "users");
        loader.loadCsvFile("/home/stanislawr/Pulpit/hotspots.csv", "hot_spots");

        CsvExporter exporter = new CsvExporter();
        exporter.exportTableToCsv("/home/stanislawr/Pulpit/usersExport.csv", "users");
        exporter.exportTableToCsv("/home/stanislawr/Pulpit/hotspotsExport.csv", "hot_spots");
    }

    private static void initializeDbConnectionFactory() {
        PropertiesLoader loader = new PropertiesLoader();
        DbConfiguration dbConfiguration = new DbConfigResolver(loader).resolve();
        DbConnectionFactory.initialize(dbConfiguration);
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
