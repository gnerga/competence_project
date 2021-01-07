package ui.traces;

import config.PropertiesLoader;

import java.nio.file.Path;
import java.util.Properties;

public class TracesGenerationConfigurationResolver {
    private static final String SRC_MAIN_RESOURCES_DB_PROPERTIES = "src/main/resources/input.properties";

    private final PropertiesLoader loader;

    public TracesGenerationConfigurationResolver(PropertiesLoader loader) {
        this.loader = loader;
    }

    public TracesGenerationConfiguration resolve() {
        Properties properties = loader.load(SRC_MAIN_RESOURCES_DB_PROPERTIES);
        var scriptPath = getPrefixedPath(properties, "pythonScript.path");
        var usersPath = getPrefixedPath(properties, "users.path");
        var hotspotsPath = getPrefixedPath(properties, "hotspots.path");
        var tracesPath = getPrefixedPath(properties, "traces.path");

        return new TracesGenerationConfiguration(scriptPath, usersPath, hotspotsPath, tracesPath);
    }

    private Path getPrefixedPath(Properties properties, String key) {
        return Path.of(properties.getProperty("traces.generation." + key));
    }
}
