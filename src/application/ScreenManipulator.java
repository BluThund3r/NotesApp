package application;

final public class ScreenManipulator {
    private ScreenManipulator() {}

    public static String readPassword() {
        char[] inputBytes = System.console().readPassword();
        return new String(inputBytes);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
