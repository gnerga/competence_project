package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectionFactory {
    private final DbConfiguration configuration;
    private static DbConnectionFactory instance;

    private DbConnectionFactory(DbConfiguration configuration) {
        this.configuration = configuration;
    }

    public static DbConnectionFactory getInstance() {
        return instance;
    }

    public static void initialize(DbConfiguration configuration) {
        instance = new DbConnectionFactory(configuration);
    }

    public static void initialize(String jdbcUrl, String userName, String password) {
        var configuration = new DbConfiguration(jdbcUrl, userName, password);
        instance = new DbConnectionFactory(configuration);
    }

    Connection connect() {
        checkIfInitialized();
        return newConnection();
    }

    Statement createStatement() {
        try {
            return connect().createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new RuntimeException("Could not create new statement!");
    }

    private void checkIfInitialized() {
        if (configuration == null) {
            throw new IllegalStateException("DbConfiguration hasn't been initialised!");
        }
    }

    private Connection newConnection() {
        try {
            return DriverManager.getConnection(configuration.jdbcUrl, configuration.userName, configuration.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not obtain new connection to database!");
    }
}
