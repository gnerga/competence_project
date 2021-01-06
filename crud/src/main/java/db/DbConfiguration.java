package db;

public class DbConfiguration {
    final String jdbcUrl;
    final String userName;
    final String password;

    public DbConfiguration(String jdbcUrl, String userName, String password) {
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }
}
