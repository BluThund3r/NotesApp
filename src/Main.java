import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

class Menu {
    private Menu() {}
    private static Menu menu = null;
    private User activeUser;
    private UserService userService;


    public static Menu getInstance() {
        if(menu == null)
            menu = new Menu();
        return menu;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }

    public void run() {

    }

    //TODO: Actiuni in meniu:
    // 1. Adauga User
    // 2. Login User
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
    // 13. Tags
    // Maybe more to come
}

class Crypto {
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

class UserService {
    User[] users;

    public void addUser(String userName, String passwordHash) {
        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = new User(userName, passwordHash);
    }

    public User getUserByUserName(String userName) {
        for(User u : users)
            if(userName.equals(u.getUserName()))
                return u;
        return null;
    }
}

// Manages the authentication for user and protected notes that the user might access
class AuthService {
    private AuthService() {}

    public static boolean loginUser(User user, String password) {
        if(!Crypto.compare(user.getPasswordHash(), password))
            return false;

        Menu.getInstance().setActiveUser(user);
        return true;
    }

    public static boolean accessProtectedNote(ProtectedGenericNote pNote, String password) {
        return Crypto.compare(pNote.getPasswordHash(), password);
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

}

public class Main {
    public static void main(String[] args) {

    }
}