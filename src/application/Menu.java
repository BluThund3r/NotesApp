package application;

import java.util.Scanner;

public class Menu {
    private Menu() {
        this.authService = new AuthService();
    }

    private static Menu menu = null;
    private User activeUser = null;

    private GenericFolder currFolder = null;
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
            }
            else if(currFolder == null) {  // There is a logged in User and there is no folder chosen
                switch (command) {
                    case 1 -> {
                        this.activeUser.showAllFolders();
                    }
                    case 2 -> {
                        this.activeUser.showAllFolders();
                        System.out.print("\nNumber of the folder you want to enter: ");
                        int noFolder = in.nextInt();
                        System.out.println(noFolder);
                        GenericFolder tempFolder = activeUser.getFolderByPosition(noFolder - 1);
                        if(tempFolder == null)
                            System.out.println("Invalid number! (No folders or number out of range)");

                        else
                            this.currFolder = tempFolder;
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
                            Folder delFolder = (Folder)activeUser.getFolderByPosition(delNumber - 1);
                            if(delFolder.getNotes() != null || delFolder.getNotes().length > 0)
                                System.out.println("THE FOLDER CONTAINS NOTES!");
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

            else {
                if(currFolder instanceof Trash) {
                    switch(command) {
                        case 1 -> {
                            this.currFolder.showAllNotes();
                        }

                        case 2 -> {
                            // Restore a note/checklist
                            this.currFolder.showAllNotes();
                            if(this.currFolder.getNotes().length == 0)
                                break;
                            System.out.print("Number of the note you want to restore: ");
                            int restoreNumber = in.nextInt();
                            GenericNote restoreNote = this.currFolder.getNoteByPosition(restoreNumber - 1);
                            if(restoreNote == null) {
                                System.out.println("Number provided is out of bounds");
                                break;
                            }
                            this.currFolder.removeNoteAtPosition(restoreNumber - 1);
                            this.activeUser.getFolderById(restoreNote.getInitialFolder().getId()).addNote(restoreNote);
                        }

                        case 3 -> {
                            // Permanently delete a note/checklist
                            this.currFolder.showAllNotes();
                            if(this.currFolder.getNotes().length == 0)
                                break;
                            System.out.print("Number of the note you want to restore: ");
                            int deleteNumber = in.nextInt();
                            this.currFolder.removeNoteAtPosition(deleteNumber - 1);
                        }

                        case 4 ->  {
                            // Clear trash
                            this.currFolder.clear();
                            System.out.println("Trash is now empty!");
                        }

                        case 5 -> {
                            this.currFolder = null;
                        }

                        case 6 -> {
                            runMenu = false;
                        }
                    }
                }

                else {
                    switch(command) {
                        case 1 ->  {
                            // Import note from file
                        }
                        case 2 ->  {
                            // Export note to file
                        }
                        case 3 ->  {
                            // Import checklist from file
                        }
                        case 4 ->  {
                            // Export checklist to file
                        }
                        case 5 ->  {
                            // Create new note from terminal
                        }
                        case 6 ->  {
                            // Create new checklist from terminal
                        }
                        case 7 ->  {
                            // Delete note/checklist (add to Trash) - From there you have to delete them permanently
                        }
                        case 8 ->  {
                            // Modify a checklist
                        }
                        case 9 ->  {
                            this.currFolder = null;
                        }
                        case 10 ->  {
                            runMenu = false;
                        }
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

    private String readPassword() {
        char[] inputBytes = System.console().readPassword();
        return new String(inputBytes);
    }

    private void showMainMenu() {
        if (this.activeUser == null) {
            System.out.println("------------------- Main Menu ------------------- ");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        } else if (currFolder == null){
            System.out.println("------------------- User Actions Menu ------------------- ");
            System.out.println("1. List Folders");
            System.out.println("2. Enter a certain folder");
            System.out.println("3. Create a folder");
            System.out.println("4. Delete a folder");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
        }
        else {
            if(this.currFolder instanceof Trash) {
                System.out.println("------------------- Trash Actions Menu ------------------- ");
                System.out.println("1. List the items in the trash");
                System.out.println("2. Restore note/checklist");
                System.out.println("3. Permanently delete a note/checklist");
                System.out.println("4. Clear trash");
                System.out.println("5. Back");
                System.out.println("6. Exit");
            }
            else {
                System.out.println("------------------- Folder '" + this.currFolder.getName() + "' Actions Menu ------------------- ");
                System.out.println("1. See al notes in the folder");
                System.out.println("2. See the content of a certain note/checklist");
                System.out.println("3. Import note from file");
                System.out.println("4. Export note to file");
                System.out.println("5. Import checklist from file");
                System.out.println("6. Export checklist to file");
                System.out.println("7. Create new note from terminal");
                System.out.println("8. Create new checklist from terminal");
                System.out.println("9. Delete note/checklist");
                System.out.println("10. Modify a checklist");
                System.out.println("11. Back to folders");
                System.out.println("12. Exit");
            }

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
