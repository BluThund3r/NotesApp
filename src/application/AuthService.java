package application;

// Manages the authentication for user and protected notes that the user might access
public final class AuthService {
    public AuthService() {}

    // returns:
    // 0 on successful login
    // -1 if there is no User with the given userName
    // -2 if the provided password does not match the User password
    public int loginUser(String userName, String password) {
        User user = UserService.getUserByUserName(userName);
        if (user == null)
            return -1;

        if (!Crypto.compare(user.getPasswordHash(), password))
            return -2;

        Menu.getInstance().setActiveUser(user);
        return 0;
    }

    // Returns true if the provided password matched the pNote password. Otherwise returns false.
    public boolean canAccessProtectedNote(ProtectedItem pNote, String password) {
        return Crypto.compare(pNote.getPasswordHash(), password);
    }

    // returns:
    // 0 on successful registration
    // -1 if the username is taken
    // -2 if the passwords do not match
    public int registerUser(String userName, String password, String confirmPassword) {
        User existingUser = UserService.getUserByUserName(userName);
        if (existingUser != null)
            return -1;
        if (!password.equals(confirmPassword))
            return -2;
        UserService.addUser(userName, password);
        return 0;
    }

    public void logoutUser() {
        Menu.getInstance().setActiveUser(null);
    }
}
