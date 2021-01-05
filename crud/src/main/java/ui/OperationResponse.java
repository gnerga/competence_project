package ui;

public class OperationResponse {
    final OperationStatus status;
    final String message;

    private OperationResponse(OperationStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    static OperationResponse success() {
        return new OperationResponse(OperationStatus.SUCCESS, null);
    }

    static OperationResponse success(String message) {
        return new OperationResponse(OperationStatus.SUCCESS, message);
    }

    static OperationResponse failure(String message) {
        return new OperationResponse(OperationStatus.FAILURE, message);
    }

    static OperationResponse failure() {
        return new OperationResponse(OperationStatus.FAILURE, null);
    }

    public enum OperationStatus {
        SUCCESS, FAILURE
    }
}
