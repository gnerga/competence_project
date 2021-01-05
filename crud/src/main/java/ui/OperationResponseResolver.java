package ui;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationResponseResolver {
    public String resolve(OperationResponse response) {
        String status = resolveStatus(response.status);
        return Stream.of(status, response.message).filter(Objects::nonNull).collect(Collectors.joining(": "));
    }

    private String resolveStatus(OperationResponse.OperationStatus status) {
        return status.name();
    }
}
