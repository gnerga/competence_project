package ui.io;

public class OneOrTwoValidator implements InputValidator<Integer> {
    private final String validationMessage;

    public OneOrTwoValidator() {
        this.validationMessage = null;
    }

    public OneOrTwoValidator(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    @Override
    public String getValidationMessage() {
        return validationMessage != null ? validationMessage : "You must select either 1 or 2!";
    }

    @Override
    public boolean isValid(Integer integer) {
        return integer == 1 || integer == 2;
    }
}
