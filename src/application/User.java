package application;

public class User {
    private String userName;
    private String passwordHash;

    Folder[] folders;

    Trash trash;

    public User(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.trash = new Trash();
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
