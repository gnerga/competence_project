package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public Properties load(String fileName) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(fileName));
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not load properties from file: " + fileName);
    }
}
