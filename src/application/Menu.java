package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
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
        ScreenManipulator.clearScreen();
        Scanner in = new Scanner(System.in);
        if (System.console() == null) {
            System.out.println("ERROR!\n" +
                    "You are not running this from a system terminal!\n" +
                    "Please, run the app from a proper terminal. (Preferably cmd or powershell)");
            return;
        }

        while (runMenu) {
            showMainMenu();
            int command = in.nextInt();
            ScreenManipulator.clearScreen();
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
                        ScreenManipulator.clearScreen();
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
                        GenericFolder tempFolder = activeUser.getFolderByPosition(noFolder - 1);
                        if(tempFolder == null)
                            System.out.println("\nInvalid number! (No folders or number out of range)");

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
                        System.out.println("\nFolder Added Successfully!");
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
                        ScreenManipulator.clearScreen();
                        System.out.println("Invalid Option!");
                    }
                }
            }

            else {
                if(currFolder instanceof Trash) {
                    switch(command) {
                        case 1 -> {
                            this.currFolder.showAllItems();
                        }

                        case 2 -> {
                            // Restore a note/checklist
                            this.currFolder.showAllItems();
                            if(this.currFolder.getItems().length == 0)
                                break;
                            System.out.print("\nNumber of the note you want to restore: ");
                            int restoreNumber = in.nextInt();
                            GenericNote restoreNote = this.currFolder.getItemByPosition(restoreNumber - 1);
                            if(restoreNote == null) {
                                System.out.println("\nNumber provided is out of bounds!");
                                break;
                            }
                            this.currFolder.removeItemAtPosition(restoreNumber - 1);
                            this.activeUser.getFolderById(restoreNote.getInitialFolder().getId()).addItem(restoreNote);
                        }

                        case 3 -> {
                            // Permanently delete a note/checklist
                            this.currFolder.showAllItems();
                            if(this.currFolder.getItems().length == 0)
                                break;
                            System.out.print("\nNumber of the note you want to PERMANENTLY DELETE: ");
                            int deleteNumber = in.nextInt();
                            this.currFolder.removeItemAtPosition(deleteNumber - 1);
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
                            ScreenManipulator.clearScreen();
                            System.out.println("Invalid Option!");
                        }
                    }
                }

                else {
                    switch(command) {
                        case 1 -> {
                            this.currFolder.showAllItems();
                        }

                        case 2 -> {
                            // See the content of a certain note/checklist
                            this.currFolder.showAllItems();
                            if(this.currFolder.getItems().length == 0) {
                                System.out.println("Create a note/checklist first!");
                                break;
                            }
                            System.out.print("Note/Checklist number: ");
                            int noNote = in.nextInt();
                            ScreenManipulator.clearScreen();
                            GenericNote note = this.currFolder.getItemByPosition(noNote - 1);
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
                            System.out.print("Type of note (p for protected / n for normal): ");
                            String noteType = in.nextLine();
                            System.out.print("Type the pathname to the file: ");
                            String pathname = in.nextLine();
                            GenericNote note = null;
                            if(noteType.equalsIgnoreCase("p")) {
                                System.out.print("Note Password: ");
                                String password = ScreenManipulator.readPassword();
                                note = new ProtectedNote(noteTitle, (Folder)this.currFolder, password);
                            }
                            else if (noteType.equalsIgnoreCase("n"))
                                note = new Note(noteTitle, (Folder)this.currFolder);

                            else {
                                System.out.println("\nInvalid option!");
                                break;
                            }

                            boolean resultStatus = note.readContentFromFile(pathname);
                            if(resultStatus) {
                                this.currFolder.addItem(note);
                                System.out.println("\nNote imported successfully!");
                            }
                            else
                                System.out.println("\nAction interrupted!\nCould not open file!");
                        }
                        case 4 ->  {
                            // Export note to file
                            this.currFolder.showAllNotes();
                            if(!this.currFolder.anyNote())
                                break;
                            System.out.print("\nNote to export: ");
                            int exportNo = in.nextInt();
                            Note note = this.currFolder.getNoteByPosition(exportNo - 1);
                            if(note == null) {
                                System.out.println("\nInvalid number! (out of bounds or specified number is not a Note)");
                                break;
                            }
                            // If the note is protected and the Authentication is not successful
                            if(note instanceof ProtectedNote && !((ProtectedNote) note).itemAuthSuccessful()) {
                                System.out.println("\nAccess denied!");
                                break;
                            }

                            System.out.print("File to export to: ");
                            in.nextLine();
                            String filePath = in.nextLine();
                            if (!note.writeContentToFile(filePath))
                                System.out.println("\nFile opening error!");
                            else {
                                System.out.println("\nNote exported successfully to file " + filePath);
                            }
                        }
                        case 5 ->  {
                            // Import Checklist from file
                            System.out.print("Checklist title: ");
                            in.nextLine();
                            String noteTitle = in.nextLine();
                            System.out.print("Type of checklist (p for protected / n for normal): ");
                            String noteType = in.nextLine();
                            System.out.print("Type the pathname to the file: ");
                            String pathname = in.nextLine();
                            GenericNote cl = null;
                            if(noteType.equalsIgnoreCase("p")) {
                                System.out.print("Checklist Password: ");
                                String password = ScreenManipulator.readPassword();
                                cl = new ProtectedCheckList(noteTitle, (Folder)this.currFolder, password);
                            }
                            else if(noteType.equalsIgnoreCase("n"))
                                cl = new CheckList(noteTitle, (Folder)this.currFolder);
                            else {
                                System.out.println("\nInvalid option!");
                                break;
                            }

                            boolean resultStatus = cl.readContentFromFile(pathname);
                            if(resultStatus) {
                                this.currFolder.addItem(cl);
                                System.out.println("\nChecklist imported successfully!");
                            }
                            else
                                System.out.println("\nAction interrupted!\nCould not open file!");
                        }
                        case 6 ->  {
                            // Export checklist to file
                            this.currFolder.showAllChecklists();
                            if(!this.currFolder.anyCheckList())
                                break;
                            System.out.print("\nChecklist to export: ");
                            int exportNo = in.nextInt();
                            CheckList cl = this.currFolder.getCheckListByPosition(exportNo - 1);
                            if(cl == null) {
                                System.out.println("\nInvalid number! (out of bounds or specified number is not a Checklist)");
                                break;
                            }
                            // If the checklist is protected and the Authentication is not successful
                            if(cl instanceof ProtectedCheckList && !((ProtectedCheckList) cl).itemAuthSuccessful())
                                break;
                            System.out.print("File to export to: ");
                            in.nextLine();
                            String filePath = in.nextLine();
                            if (!cl.writeContentToFile(filePath))
                                System.out.println("\nFile opening error!");
                            else {
                                System.out.println("\nChecklist exported successfully to file " + filePath);
                            }
                        }
                        case 7 ->  {
                            // Create new note from terminal
                            System.out.println("=============== CREATE A NOTE ===============");
                            System.out.println("Instructions:");
                            System.out.println("- Write each line of your note on a line in the terminal");
                            System.out.println("- If you want to cancel the note just type ::cancel:: (non case sensitive) on a new line");
                            System.out.println("- If you are done and want to save the note just type ::end:: (non case sensitive) on a new line");
                            System.out.println("=============================================");
                            in.nextLine();
                            System.out.print("Note Title: ");
                            String noteTitle = in.nextLine();
                            if(noteTitle.equals(""))
                                noteTitle = "Untitled Note";
                            System.out.print("Type of note (p for protected / n for normal): ");
                            String noteType = in.next();
                            String password = null;
                            if(noteType.equalsIgnoreCase("p")) {
                                System.out.print("Password: ");
                                password = ScreenManipulator.readPassword();
                            }
                            else if(!noteType.equalsIgnoreCase("n")) {
                                ScreenManipulator.clearScreen();
                                System.out.println("Invalid option!");
                                break;
                            }

                            System.out.println("---------------------------------------------");
                            String[] noteContent = null;
                            in.nextLine();
                            while(true) {
                                String line = in.nextLine();
                                if(line.equalsIgnoreCase("::cancel::")) {
                                    ScreenManipulator.clearScreen();
                                    System.out.println("Action Cancelled!");
                                    break;
                                }

                                if(line.equalsIgnoreCase("::end::")) {
                                    ScreenManipulator.clearScreen();
                                    if(noteContent == null)
                                        System.out.println("Empty note was not saved!");

                                    else {
                                        GenericNote noteToAdd = null;
                                        if(password != null)
                                            noteToAdd = new ProtectedNote(noteTitle, noteContent, (Folder)this.currFolder, password);
                                        else
                                            noteToAdd = new Note(noteTitle, noteContent, (Folder)this.currFolder);
                                        this.currFolder.addItem(noteToAdd);
                                        System.out.println("Note saved!");
                                    }
                                    break;
                                }

                                if(noteContent == null || !line.strip().equals(""))
                                    noteContent = new String[0];


                                if(noteContent != null) {
                                    noteContent = Arrays.copyOf(noteContent, noteContent.length + 1);
                                    noteContent[noteContent.length - 1] = line;
                                }
                            }

                        }
                        case 8 ->  {
                            // Create new checklist from terminal
                            System.out.println("=============== CREATE A NOTE ===============");
                            System.out.println("Instructions:");
                            System.out.println("- Write each element of your checklist on a line in the terminal");
                            System.out.println("- If you want to cancel the checklist just type ::cancel:: (non case sensitive) on a new line");
                            System.out.println("- If you are done and want to save the checklist just type ::end:: (non case sensitive) on a new line");
                            System.out.println("=============================================");
                            in.nextLine();
                            System.out.print("Checklist Title: ");
                            String noteTitle = in.nextLine();
                            if(noteTitle.equals(""))
                                noteTitle = "Untitled Checklist";
                            System.out.print("Type of note (p for protected / n for normal): ");
                            String noteType = in.nextLine();
                            String password = null;
                            if(noteType.equalsIgnoreCase("p")){
                                System.out.print("Password: ");
                                password = ScreenManipulator.readPassword();
                            }

                            else if(!noteType.equalsIgnoreCase("n")) {
                                ScreenManipulator.clearScreen();
                                System.out.println("Invalid option!");
                                break;
                            }

                            System.out.println("---------------------------------------------");
                            String[] noteContent = null;
                            while(true) {
                                String line = in.nextLine();
                                if(line.equalsIgnoreCase("::cancel::")) {
                                    ScreenManipulator.clearScreen();
                                    System.out.println("Action Cancelled!");
                                    break;
                                }

                                if(line.equalsIgnoreCase("::end::")) {
                                    ScreenManipulator.clearScreen();
                                    if(noteContent == null)
                                        System.out.println("Empty checklist was not saved!");

                                    else {
                                        GenericNote noteToAdd = null;
                                        if(password != null)
                                            noteToAdd = new ProtectedCheckList(noteTitle, noteContent, (Folder)this.currFolder, password);
                                        else
                                            noteToAdd = new CheckList(noteTitle, noteContent, (Folder)this.currFolder);
                                        this.currFolder.addItem(noteToAdd);
                                        System.out.println("Checklist saved!");
                                    }
                                    break;
                                }

                                if(noteContent == null) {
                                    noteContent = new String[0];
                                }
                                noteContent = Arrays.copyOf(noteContent, noteContent.length + 1);
                                noteContent[noteContent.length - 1] = line;
                            }
                        }
                        case 9 ->  {
                            // Delete note/checklist (add to Trash) - From there you have to delete them permanently
                            this.currFolder.showAllItems();
                            System.out.print("Item to move to trash: ");
                            int noItem = in.nextInt();
                            GenericNote note = this.currFolder.getItemByPosition(noItem - 1);
                            if(note == null) {
                                System.out.println("\nInvalid number! (out of range)");
                                break;
                            }
                            ScreenManipulator.clearScreen();
                            if(note.allowDelete()) {
                                this.activeUser.getTrash().addItem(note);
                                this.currFolder.removeItemAtPosition(noItem - 1);
                                System.out.println("\nItem moved to trash!");
                            }
                            else
                                System.out.println("\nAccess denied!");
                        }
                        case 10 ->  {
                            // Check/uncheck items in a checklist
                            this.currFolder.showAllChecklists();
                            System.out.print("\nChecklist to edit: ");
                            int noChecklist = in.nextInt();
                            CheckList checkList = this.currFolder.getCheckListByPosition(noChecklist - 1);
                            if(checkList == null) {
                                System.out.println("\nInvalid number!");
                                break;
                            }
                            ScreenManipulator.clearScreen();
                            checkList.toggleElements();
                        }
                        case 11 ->  {
                            this.currFolder = null;
                        }
                        case 12 ->  {
                            runMenu = false;
                        }
                        default -> {
                            ScreenManipulator.clearScreen();
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
                System.out.println("1. See all notes in the folder");
                System.out.println("2. See the content of a certain note/checklist");
                System.out.println("3. Import note from file");
                System.out.println("4. Export note to file");
                System.out.println("5. Import checklist from file");
                System.out.println("6. Export checklist to file");
                System.out.println("7. Create new note from terminal");
                System.out.println("8. Create new checklist from terminal");
                System.out.println("9. Move note/checklist to trash");
                System.out.println("10. Check/uncheck items in a checklist");
                System.out.println("11. Back to folders");
                System.out.println("12. Exit");
            }

        }
        System.out.print("\nType the number associated with the desired action: ");
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
