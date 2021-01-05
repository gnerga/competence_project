package ui.io;

import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class CLIReader {
    public int readInt(String errorMessage) {
        while (true) {
            try {
                String line = readLine();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public int readInt(Function<String, String> errorMessage) {
        while (true) {
            String line = readLine();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage.apply(line));
            }
        }
    }

    public int readInt(InputValidator<Integer> validator, String errorMessage) {
        while (true) {
            int integer = readInt(errorMessage);
            if (validator.isValid(integer)) {
                return integer;
            } else {
                System.out.println(validator.getValidationMessage());
            }
        }
    }

    public int readInt(InputValidator<Integer> validator, Function<String, String> errorMessage) {
        while (true) {
            int integer = readInt(errorMessage);
            if (validator.isValid(integer)) {
                return integer;
            } else {
                System.out.println(validator.getValidationMessage());
            }
        }
    }

    public float readFloat(String errorMessage) {
        while (true) {
            try {
                String line = readLine();
                return Float.parseFloat(line);
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public float readFloat(InputValidator<Float> validator, String errorMessage) {
        while (true) {
            float parsedFloat = readFloat(errorMessage);

            if (validator.isValid(parsedFloat)) {
                return parsedFloat;
            } else {
                System.out.println(validator.getValidationMessage());
            }
        }
    }

    public String readString() {
        return readLine();
    }

    public String readString(Predicate<String> validator, String validationMessage) {
        while (true) {
            String string = readLine();
            if (validator.test(string)) {
                return string;
            } else {
                System.out.println(validationMessage);
            }
        }
    }


    private String readLine() {
        Scanner in = new Scanner(System.in);
        return in.nextLine().trim();
    }
}
