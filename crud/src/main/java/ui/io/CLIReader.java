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

    public int readInt(Predicate<Integer> validator, String validationMessage, String errorMessage) {
        while (true) {
            int integer = readInt(errorMessage);
            if (validator.test(integer)) {
                return integer;
            } else {
                System.out.println(validationMessage);
            }
        }
    }

    public int readInt(Predicate<Integer> validator, String validationMessage, Function<String, String> errorMessage) {
        while (true) {
            int integer = readInt(errorMessage);
            if (validator.test(integer)) {
                return integer;
            } else {
                System.out.println(validationMessage);
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

    public float readFloat(Predicate<Float> validator, String validationMessage, String errorMessage) {
        while (true) {
            float parsedFloat = readFloat(errorMessage);

            if (validator.test(parsedFloat)) {
                return parsedFloat;
            } else {
                System.out.println(validationMessage);
            }
        }
    }

    public String readString() {
        return readLine();
    }

    private String readLine() {
        Scanner in = new Scanner(System.in);
        return in.nextLine().trim();
    }
}
