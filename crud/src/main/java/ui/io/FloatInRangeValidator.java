package ui.io;

public class FloatInRangeValidator implements InputValidator<Float> {
    private final float min;
    private final float max;

    public FloatInRangeValidator(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String getValidationMessage() {
        return "Provided value must be in range [" + min + "; " + max + "]!";
    }

    @Override
    public boolean isValid(Float aFloat) {
        return aFloat >= min && aFloat <= max;
    }
}
