package application;

import java.util.Arrays;

public class User {
    private String userName;
    private String passwordHash;

    private Folder[] folders;

    private Trash trash;

    {
        this.folders = new Folder[0];
        this.trash = new Trash();
    }

    public User(String userName, String passwordHash) {
        this.userName = userName;
        this.passwordHash = passwordHash;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void addFolder(String title) {
        if(this.folders == null)
            this.folders = new Folder[1];
        else
            this.folders = Arrays.copyOf(this.folders, this.folders.length + 1);
        this.folders[this.folders.length - 1] = new Folder(title);
    }

    public void deleteFolder(int pos) {
        if(this.folders == null || pos >= this.folders.length)
            return;
        if(pos < this.folders.length - 1)
            for(int i = pos; i < this.folders.length - 1; ++ i)
                this.folders[i] = this.folders[i + 1];

        this.folders = Arrays.copyOf(this.folders, this.folders.length - 1);
    }

    // For testing purposes
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }

    public Folder[] getFolders() {
        return this.folders;
    }

    public Trash getTrash() {
        return this.trash;
    }

    public void showAllFolders() {
        System.out.println("\nAll Folders: ");
        int noFolders = 0;
        if(folders != null)
            for(int i = 0; i < folders.length; ++ i) {
                System.out.println((i + 1) + ". " + folders[i].getName());
                noFolders ++;
            }

        System.out.println((noFolders + 1) + ". " + trash.getName());
    }

    // Returns the Folder at the specified position(index) in folders[] or null if can't access the position in the array
    // (null array or position out of array bounds)
    public GenericFolder getFolderByPosition(int position) {
        try {
            return this.folders[position];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            if(position == this.folders.length)
                return this.trash;
            return null;
        }
    }

    public Folder getFolderById(int id) {
        for(Folder folder : folders)
            if(folder.getId() == id)
                return folder;
        return null;
    }
}
