package ui.io;

interface InputValidator<T> {
    String getValidationMessage();

    boolean isValid(T t);
}
