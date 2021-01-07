package ui.traces;

import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.io.OneOrTwoValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TracesController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final TracesGenerationConfigurationResolver configurationResolver;
    private final CLIReader cliReader;

    public TracesController(CLIReader cliReader, OperationResponseResolver responseResolver, TracesGenerationConfigurationResolver configurationResolver) {
        this.cliReader = cliReader;
        this.responseResolver = responseResolver;
        this.configurationResolver = configurationResolver;
    }

    @Override
    public void run() {
        TracesOperation operation = selectOperation();
        if (TracesOperation.GO_BACK == operation) {
            return;
        }

        print(responseResolver.resolve(execute(operation)));
    }

    private TracesOperation selectOperation() {
        print("");
        print("1. Generate traces");
        print("2. Go back");

        int selected = cliReader.readInt(new OneOrTwoValidator(), "That's not an integer");

        if (selected == 1) {
            return TracesOperation.GENERATE;
        }

        if (selected == 2) {
            return TracesOperation.GO_BACK;
        }

        throw new IllegalStateException("Option should've been already selected");
    }

    private OperationResponse execute(TracesOperation operation) {
        if (TracesOperation.GENERATE == operation) return generateTraces();

        throw new IllegalStateException("Such operation cannot be handled!");
    }

    private OperationResponse generateTraces() {
        print("");
        print("Generating traces...");

        try {
            Process proc = Runtime.getRuntime().exec(getCommandWithParameters());

            try (BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                String error = stdError.readLine();
                if (error != null) {
                    StringBuilder errorBuilder = new StringBuilder("Error occurred during traces generation! Here are python script's logs:\n");
                    errorBuilder.append(error);
                    errorBuilder.append("\n");
                    while ((error = stdError.readLine()) != null) {
                        errorBuilder.append(error);
                        errorBuilder.append("\n");
                    }

                    return OperationResponse.failure(errorBuilder.toString());
                }
            } catch (IOException ignored) {
            }

            return OperationResponse.success("Traces generated!");
        } catch (IOException e) {
            e.printStackTrace();
            return OperationResponse.failure("Could not generate traces!");
        }
    }

    private String[] getCommandWithParameters() {
        var configuration = configurationResolver.resolve();
        return new String[]{
                "python3", configuration.pythonScriptPath.toString(),
                "-l", "-e",
                "-su", configuration.usersLocation.toString(),
                "-sh", configuration.hotspotsLocation.toString(),
                "-st", configuration.tracesLocation.toString(),
        };
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
