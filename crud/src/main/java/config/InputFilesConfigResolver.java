package config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class InputFilesConfigResolver {
    public static final String SRC_MAIN_RESOURCES_DB_PROPERTIES = "src/main/resources/input.properties";

    private final PropertiesLoader loader;

    public InputFilesConfigResolver(PropertiesLoader loader) {
        this.loader = loader;
    }

    public Path getFakePhoneNumbersLocation() {
        Properties properties = loader.load(SRC_MAIN_RESOURCES_DB_PROPERTIES);
        String path = properties.getProperty("input.fakePhoneNumbers.path");
        return Paths.get(path);
    }
}