package domain.hotspots;

class ValidationError {
    public final String reason;

    ValidationError(String reason) {
        this.reason = reason;
    }
}
