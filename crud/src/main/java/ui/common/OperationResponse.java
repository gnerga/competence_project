package ui.common;

public class OperationResponse {
    final OperationStatus status;
    final String message;

    private OperationResponse(OperationStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static OperationResponse success() {
        return new OperationResponse(OperationStatus.SUCCESS, null);
    }

    public static OperationResponse success(String message) {
        return new OperationResponse(OperationStatus.SUCCESS, message);
    }

    public static OperationResponse failure(String message) {
        return new OperationResponse(OperationStatus.FAILURE, message);
    }

    public static OperationResponse failure() {
        return new OperationResponse(OperationStatus.FAILURE, null);
    }

    public enum OperationStatus {
        SUCCESS, FAILURE
    }
}
