package application;

import java.util.Scanner;

public class Menu {
    private Menu() {
        this.authService = new AuthService();
    }

    private static Menu menu = null;
    private User activeUser = null;
    private AuthService authService;

    public static Menu getInstance() {
        if (menu == null)
            menu = new Menu();
        return menu;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }

    public void run() {
        boolean runMenu = true;
        Scanner in = new Scanner(System.in);
        if (System.console() == null) {
            System.out.println("ERROR!\n" +
                    "You are not running this from a system terminal!\n" +
                    "Please, run the app from a proper terminal.");
            return;
        }

        while (runMenu) {
            showMainMenu();
            int command = in.nextInt();
            if (activeUser == null) {        // There is NO logged in User
                switch (command) {
                    case 1 -> {
                        System.out.println("\nLOGIN");
                        System.out.print("Username: ");
                        String userName = in.next();
                        System.out.print("Password: ");
                        String inputPass = readPassword();
                        int authResult = authService.loginUser(userName, inputPass);
                        showLoginResult(authResult);
                    }

                    case 2 -> {
                        System.out.println("\nREGISTER");
                        System.out.print("Username: ");
                        String userName = in.next();
                        System.out.print("Password: ");
                        String inputPass = readPassword();
                        System.out.print("Confirm Password: ");
                        String confirmPass = readPassword();
                        int registerResult = authService.registerUser(userName, inputPass, confirmPass);
                        showRegisterResult(registerResult);
                    }

                    case 3 -> {
                        runMenu = false;
                    }
                }
            } else {  // There is a logged in User
                switch (command) {
                    case 1 -> {
                        this.activeUser.showAllFolders();
                    }
                    case 2 -> {
                        // Enter a folder
                    }
                    case 3 -> {
                        in.nextLine();
                        System.out.print("New Folder Name: ");
                        String fName = in.nextLine();
                        if(fName.strip().equals("")) {
                            System.out.println("Folder name must not be empty!");
                            break;
                        }

                        activeUser.addFolder(fName);
                        System.out.println("Folder Added Successfully!");
                    }
                    case 4 -> {
                        activeUser.showAllFolders();
                        if(activeUser.getFolders() == null || activeUser.getFolders().length == 0) {
                            System.out.println("No folders to delete!");
                            break;
                        }

                        System.out.print("Number of the folder to delete: ");
                        int delNumber = in.nextInt();
                        if(delNumber < 0 || delNumber > activeUser.getFolders().length + 1)
                            System.out.println("Invalid number!");
                        else if(delNumber == activeUser.getFolders().length + 1)
                            System.out.println("Can't delete Trash Folder!");
                        else {
                            System.out.print("Are you sure you want to delete this folder? (y/n): ");
                            String option = in.next();
                            if(option.equalsIgnoreCase("y")) {
                                this.activeUser.deleteFolder(delNumber - 1);
                                System.out.println("Folder deleted!");
                            }


                            else if(option.equalsIgnoreCase("n"))
                                System.out.println("OK! Action Canceled!");

                            else
                                System.out.println("Invalid Option! Action Cancelled!");
                        }
                    }
                    case 5 -> {
                        authService.logoutUser();
                        System.out.println("LOGGED OUT!");
                    }
                    case 6 -> {
                        runMenu = false;
                    }
                }
            }

            if (runMenu) {
                pressEnterToContinue();
                clearScreen();
            }
        }
        System.out.println("\n=============== QUITING THE APPLICATION ===============");
    }

    //TODO: Actiuni in meniu:
    // 1. Adauga User - DONE
    // 2. Login User - DONE
    // 3. Vizualizeaza o lista cu notitele user-ului
    // 4. Vizualizeaza continutul unei notite specificate de catre user
    // 5. Adauga o notita
    // 6. Sterge o notita
    // 7. Importa notita din fisier
    // 8. Exporta notita in fisier
    // 9. Logout User
    // 10. Creare folder
    // 11. Sterge folder
    // 12. Adauga notita in folder
    // Maybe more to come

    private String readPassword() {
        char[] inputBytes = System.console().readPassword();
        return new String(inputBytes);
    }

    private void showMainMenu() {
        System.out.println("------------------- Main Menu ------------------- ");
        if (this.activeUser == null) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        } else {
            System.out.println("1. List Folders");
            System.out.println("2. Enter a certain folder");
            System.out.println("3. Create a folder");
            System.out.println("4. Delete a folder");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
        }
        System.out.print("Type the number associated with the desired action: ");
    }

    private void showRegisterResult(int registerResult) {
        switch (registerResult) {
            case 0 -> {
                System.out.println("REGISTER SUCCESSFUL!");
            }
            case -1 -> {
                System.out.println("USERNAME ALREADY TAKEN!");
            }
            case -2 -> {
                System.out.println("PASSWORDS DO NOT MATCH!");
            }
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pressEnterToContinue() {
        System.out.println("\nPress ENTER to continue...");

        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFolderListMenu() {

    }

    private void showLoginResult(int authResult) {
        switch (authResult) {
            case 0 -> {
                System.out.println("LOGIN SUCCESSFUL!");
            }
            case -1 -> {
                System.out.println("USERNAME NOT FOUND!");
            }
            case -2 -> {
                System.out.println("WRONG PASSWORD!");
            }
        }
    }
}
