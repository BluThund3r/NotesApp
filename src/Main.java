import java.io.Console;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

class Menu {
    private Menu() {
        this.authService = new AuthService();
    }
    private static Menu menu = null;
    private User activeUser = null;
    private AuthService authService;

    public static Menu getInstance() {
        if(menu == null)
            menu = new Menu();
        return menu;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }

    public void run() {
        boolean runMenu = true;
        Scanner in = new Scanner(System.in);
        if(System.console() == null) {
            System.out.println("ERROR!\n" +
                    "You are not running this from a system terminal!\n" +
                    "Please, run the app from a proper terminal.");
            return;
        }

        while(runMenu) {
            showMainMenu();
            int command = in.nextInt();
            if(activeUser == null) {        // There is NO logged in User
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

            else {  // There is a logged in User

            }

            if(runMenu) {
                pressEnterToContinue();
                clearScreen();
            }
        }
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
        if(this.activeUser == null) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        }

        else {
            System.out.println("1. See all the folders");
            System.out.println("2. Enter a certain folder");
            System.out.println("3. Create a folder");
            System.out.println("4. Delete a folder");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
        }
        System.out.print("Type the number associated with the desired action: ");
    }

    private void showRegisterResult(int registerResult) {
        switch(registerResult) {
            case 0 -> {
                System.out.println("REGISTER SUCCESSFUL!");
            }
            case -1 -> {
                System.out.println("USERNAME ALREADY TAKEN!");
            }
            case -2 ->  {
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
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showFolderListMenu() {

    }

    private void showLoginResult(int authResult) {
        switch(authResult) {
            case 0 -> {
                System.out.println("LOGIN SUCCESSFUL!");
            }
            case -1 -> {
                System.out.println("USERNAME NOT FOUND!");
            }
            case -2 ->  {
                System.out.println("WRONG PASSWORD!");
            }
        }
    }
}

final class Crypto {
    private Crypto() {}
    public static String encryptPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            byte[] resultByteArray = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for(byte b : resultByteArray) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean compare(String hashPassword, String plainPassword) {
        String resultHashPassword = Crypto.encryptPassword(plainPassword);
        return hashPassword.equals(resultHashPassword);
    }
}

final class UserService {
    private static User[] users;

    private UserService() {}
    public static void addUser(String userName, String password) {
        if(users == null)
            users = new User[0];
        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = new User(userName, Crypto.encryptPassword(password));
    }

    public static User getUserByUserName(String userName) {
        if(users == null)
            return null;
        for(User u : users)
            if(userName.equals(u.getUserName()))
                return u;
        return null;
    }
}

// Manages the authentication for user and protected notes that the user might access
final class AuthService {
    public AuthService() {}

    // returns:
    // 0 on successful login
    // -1 if there is no User with the given userName
    // -2 if the provided password does not match the User password
    public int loginUser(String userName, String password) {
        User user = UserService.getUserByUserName(userName);
        if(user == null)
            return -1;

        if(!Crypto.compare(user.getPasswordHash(), password))
            return -2;

        Menu.getInstance().setActiveUser(user);
        return 0;
    }

    // Returns true if the provided password matched the pNote password. Otherwise returns false.
    public boolean accessProtectedNote(ProtectedGenericNote pNote, String password) {
        return Crypto.compare(pNote.getPasswordHash(), password);
    }

    // returns:
    // 0 on successful registration
    // -1 if the username is taken
    // -2 if the passwords do not match
    public int registerUser(String userName, String password, String confirmPassword) {
        User existingUser = UserService.getUserByUserName(userName);
        if(existingUser != null)
            return -1;
        if(!password.equals(confirmPassword))
            return -2;
        UserService.addUser(userName, password);
        return 0;
    }
}

abstract class GenericFolder {
    private String name;
}

class Trash extends GenericFolder{

}

class Folder extends GenericFolder{

}

abstract class GenericNote {
    private String title;
    private Date dateCreated;
    private Date dateModified;

    public GenericNote() {
        dateCreated = new Date();
        dateModified = new Date();
        this.title = "";
    }

    public GenericNote(String title) {
        dateCreated = new Date();
        dateModified = new Date();
        this.title = title;
    }
}

abstract class ProtectedGenericNote extends GenericNote{
    String passwordHash;

    public String getPasswordHash(){
        return this.passwordHash;
    }
}

class Note extends GenericNote{

}

class CheckList extends GenericNote {

}

class ProtectedNote extends ProtectedGenericNote {

}

class ProtectedCheckList extends ProtectedGenericNote {

}

class CheckListElement {

}

class User {
    private String userName;
    private String passwordHash;

    public User(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public String getUserName() { return this.userName; }

    public String getPasswordHash() { return this.passwordHash; }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}

public class Main {
    public static void main(String[] args) {
        Menu menu = Menu.getInstance();
        menu.run();
    }
}