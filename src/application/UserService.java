package application;

import java.util.Arrays;

public final class UserService {
    private static User[] users;

    private UserService() {
    }

    public static void addUser(String userName, String password) {
        if (users == null)
            users = new User[0];
        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = new User(userName, Crypto.encryptPassword(password));
    }

    public static User getUserByUserName(String userName) {
        if (users == null)
            return null;
        for (User u : users)
            if (userName.equals(u.getUserName()))
                return u;
        return null;
    }
}
