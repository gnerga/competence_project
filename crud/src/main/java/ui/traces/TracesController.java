package ui.traces;

import ui.common.OperationResponse;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;
import ui.io.OneOrTwoValidator;

import java.io.IOException;

public class TracesController implements Runnable {
    private final OperationResponseResolver responseResolver;
    private final CLIReader cliReader;

    public TracesController(CLIReader cliReader, OperationResponseResolver responseResolver) {
        this.cliReader = cliReader;
        this.responseResolver = responseResolver;
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
            Runtime.getRuntime().exec(new String[]{"python3", "test.py"});
            return OperationResponse.success("Traces generated!");
        } catch (IOException e) {
            e.printStackTrace();
            return OperationResponse.failure("Could not generate traces!");
        }
    }

    private void print(Object object) {
        System.out.println(object);
    }
}
