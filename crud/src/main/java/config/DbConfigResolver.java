package config;

import db.DbConfiguration;

import java.util.Properties;

public class DbConfigResolver {
    public static final String SRC_MAIN_RESOURCES_DB_PROPERTIES = "src/main/resources/db.properties";

    private final PropertiesLoader loader;

    public DbConfigResolver(PropertiesLoader loader) {
        this.loader = loader;
    }

    public DbConfiguration resolve() {
        Properties properties = loader.load(SRC_MAIN_RESOURCES_DB_PROPERTIES);
        String url = properties.getProperty("db.url", "jdbc:mysql://127.0.0.1:3307/GoodHotDog");
        String userName = properties.getProperty("db.username", "root");
        String password = properties.getProperty("db.password", "root");

        return new DbConfiguration(url, userName, password);
    }
}
