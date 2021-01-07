package ui.traces;

import java.nio.file.Path;

public class TracesGenerationConfiguration {
    public final Path pythonScriptPath;
    public final Path usersLocation;
    public final Path hotspotsLocation;
    public final Path tracesLocation;

    public TracesGenerationConfiguration(Path pythonScriptPath, Path usersLocation, Path hotspotsLocation, Path tracesLocation) {
        this.pythonScriptPath = pythonScriptPath;
        this.usersLocation = usersLocation;
        this.hotspotsLocation = hotspotsLocation;
        this.tracesLocation = tracesLocation;
    }
}
