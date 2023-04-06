package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Menu {
    private Menu() {
        this.authService = new AuthService();
    }

    private static Menu menu = null;
    private User activeUser = null;

    private GenericFolder currFolder = null;
    final private AuthService authService;

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
                        String inputPass = ScreenManipulator.readPassword();
                        int authResult = authService.loginUser(userName, inputPass);
                        showLoginResult(authResult);
                    }

                    case 2 -> {
                        System.out.println("\nREGISTER");
                        System.out.print("Username: ");
                        String userName = in.next();
                        System.out.print("Password: ");
                        String inputPass = ScreenManipulator.readPassword();
                        System.out.print("Confirm Password: ");
                        String confirmPass = ScreenManipulator.readPassword();
                        int registerResult = authService.registerUser(userName, inputPass, confirmPass);
                        showRegisterResult(registerResult);
                    }

                    case 3 -> {
                        runMenu = false;
                    }
                    default -> {
                        System.out.println("Invalid Option!");
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
                            if(delFolder.getItems() != null || delFolder.getItems().length > 0)
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
                    default -> {
                        System.out.println("Invalid Option!");
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
                            if(this.currFolder.getItems().length == 0)
                                break;
                            System.out.print("Number of the note you want to restore: ");
                            int restoreNumber = in.nextInt();
                            GenericNote restoreNote = this.currFolder.getItemByPosition(restoreNumber - 1);
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
                            if(this.currFolder.getItems().length == 0)
                                break;
                            System.out.print("Number of the note you want to PERMANENTLY DELETE: ");
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
                        default -> {
                            System.out.println("Invalid Option!");
                        }
                    }
                }

                else {
                    switch(command) {
                        case 1 -> {
                            this.currFolder.showAllNotes();
                        }

                        case 2 -> {
                            // See the content of a certain note/checklist
                            this.currFolder.showAllNotes();
                            if(this.currFolder.getItems().length == 0) {
                                System.out.println("Create a note/checklist first!");
                                break;
                            }
                            System.out.print("Note/Checklist number: ");
                            int noNote = in.nextInt();
                            GenericNote note = this.currFolder.getItemByPosition(noNote);
                            if(note == null) {
                                System.out.println("Invalid number! (out of range)");
                                break;
                            }
                            note.showContent();
                        }

                        case 3 ->  {
                            // Import note from file
                            System.out.print("Note title: ");
                            in.nextLine();
                            String noteTitle = in.nextLine();
                            System.out.println("Type of note (p/n): ");
                            String noteType = in.next();
                            System.out.print("Type the pathname to the file: ");
                            String pathname = in.nextLine();
                            GenericNote note = null;
                            if(noteType.equalsIgnoreCase("p")) {
                                System.out.print("Note Password: ");
                                String password = ScreenManipulator.readPassword();
                                note = new ProtectedNote(noteTitle, (Folder)this.currFolder, password);
                            }
                            else
                                note = new Note(noteTitle, (Folder)this.currFolder);

                            boolean resultStatus = note.readContentFromFile(pathname);
                            if(resultStatus) {
                                this.currFolder.addNote(note);
                                System.out.println("Note imported successfully!");
                            }
                            else
                                System.out.println("Action interrupted!\nCould not open file!");
                        }
                        case 4 ->  {
                            // Export note to file
                            this.currFolder.showAllNotes();
                            System.out.print("Note to export: ");
                            int exportNo = in.nextInt();
                            Note note = this.currFolder.getNoteByPosition(exportNo - 1);
                            if(note == null) {
                                System.out.println("Invalid number! (out of bounds or specified number is not a Note)");
                                break;
                            }
                            System.out.print("File to export to");
                            // TODO: not done yet
                        }
                        case 5 ->  {
                            // Import Checklist from file
                            System.out.print("Checklist title: ");
                            in.nextLine();
                            String noteTitle = in.nextLine();
                            System.out.println("Type of checklist (p/n): ");
                            String noteType = in.next();
                            System.out.print("Type the pathname to the file: ");
                            String pathname = in.nextLine();
                            GenericNote cl = null;
                            if(noteType.equalsIgnoreCase("p")) {
                                System.out.print("Checklist Password: ");
                                String password = ScreenManipulator.readPassword();
                                cl = new ProtectedCheckList(noteTitle, (Folder)this.currFolder, password);
                            }
                            else
                                cl = new CheckList(noteTitle, (Folder)this.currFolder);

                            boolean resultStatus = cl.readContentFromFile(pathname);
                            if(resultStatus) {
                                this.currFolder.addNote(cl);
                                System.out.println("Checklist imported successfully!");
                            }
                            else
                                System.out.println("Action interrupted!\nCould not open file!");
                        }
                        case 6 ->  {
                            // Export checklist to file
                        }
                        case 7 ->  {
                            // Create new note from terminal
                        }
                        case 8 ->  {
                            // Create new checklist from terminal
                        }
                        case 9 ->  {
                            // Delete note/checklist (add to Trash) - From there you have to delete them permanently
                            this.currFolder.showAllItems();
                            System.out.print("Item to move to trash: ");
                            int noItem = in.nextInt();
                            GenericNote note = this.currFolder.getNoteByPosition(noItem - 1);
                            if(note == null) {
                                System.out.println("Invalid number! (out of range)");
                                break;
                            }
                            if(note.allowDelete()) {
                                this.activeUser.getTrash().addNote(note);
                                this.currFolder.removeNoteAtPosition(noItem - 1);
                                System.out.println("Item moved to trash!");
                            }
                            else
                                System.out.println("Access denied!");
                        }
                        case 10 ->  {
                            // Modify a checklist
                        }
                        case 11 ->  {
                            this.currFolder = null;
                        }
                        case 12 ->  {
                            runMenu = false;
                        }
                        default -> {
                            System.out.println("Invalid Option!");
                        }
                }

                }
            }

            if (runMenu) {
                ScreenManipulator.pressEnterToContinue();
                ScreenManipulator.clearScreen();
            }
        }
        System.out.println("\n=============== QUITTING THE APPLICATION ===============");
    }

    private void showMainMenu() {
        if (this.activeUser == null) {
            System.out.println("------------------- Main Menu ------------------- ");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        }
        else if (currFolder == null){
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
