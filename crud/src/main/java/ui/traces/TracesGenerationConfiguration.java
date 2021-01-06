package ui.traces;

import config.PropertiesLoader;

import java.nio.file.Path;
import java.util.Properties;

public class TracesGenerationConfiguration {
    public static final String SRC_MAIN_RESOURCES_DB_PROPERTIES = "src/main/resources/input.properties";

    public final Path pythonScriptPath;

    public TracesGenerationConfiguration(PropertiesLoader loader) {
        Properties properties = loader.load(SRC_MAIN_RESOURCES_DB_PROPERTIES);
        this.pythonScriptPath = Path.of(properties.getProperty("traces.generation.pythonScript.path"));
    }
}
