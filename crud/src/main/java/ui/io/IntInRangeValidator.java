package ui.io;

public class IntInRangeValidator implements InputValidator<Integer> {
    private final int min;
    private final int max;

    public IntInRangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String getValidationMessage() {
        return "Provided value must be in range [" + min + "; " + max + "]!";
    }

    @Override
    public boolean isValid(Integer integer) {
        return integer >= min && integer <= max;
    }
}
