package application;

import java.io.IOException;

final public class ScreenManipulator {
    private ScreenManipulator() {}

    public static String readPassword() {
        char[] inputBytes = System.console().readPassword();
        return new String(inputBytes);
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            System.out.println("Runtime Exception during screen clearing");
        } catch (IOException e) {
            System.out.println("IO Exception during screen clearing");
        }
    }

    public static void pressEnterToContinue() {
        System.out.println("\nPress ENTER to continue...");

        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
