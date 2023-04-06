package application;

public class ProtectedNote extends Note implements ProtectedItem {
    private String passwordHash;

    public ProtectedNote(String title, String[] content, Folder initialFolder, String password) {
        super(title, content, initialFolder);
        this.passwordHash = Crypto.encryptPassword(password);
    }

    public ProtectedNote(String title, Folder initialFolder, String password) {
        super(title, initialFolder);
        this.passwordHash = Crypto.encryptPassword(password);
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    @Override
    public boolean itemAuthSuccessful() {
        AuthService authService = new AuthService();
        int tries = 3;
        System.out.println("Protected by password!");
        while(tries > 0) {
            System.out.println(tries + " tries left");
            System.out.print("Note Password: ");

            String password = ScreenManipulator.readPassword();
            if (authService.canAccessProtectedNote(this, password)) {
                return true;
            }

            tries--;
        }
        return false;
    }

    @Override
    public void showContent() {
        if(itemAuthSuccessful())
            super.showContent();
        else
            System.out.println("Access denied!");
    }

    @Override
    public boolean allowDelete() {
        return itemAuthSuccessful();
    }
}
