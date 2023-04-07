package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Note extends GenericNote {
    private String[] content;

    {
        this.content = new String[0];
    }

    public Note(String title, String[] content, Folder initialFolder) {
        super(title, initialFolder);
        this.content = new String[content.length];
        for(int i = 0; i < content.length; ++ i)
            this.content[i] = content[i];
    }

    public Note(String title, Folder initialFolder) {
        super(title, initialFolder);
    }

    @Override
    public void showContent() {
        ScreenManipulator.clearScreen();
        System.out.println("==================== " + this.getTitle() + " ====================");
        for(String line : content)
            System.out.println(line);
        System.out.println();
    }

    @Override
    public boolean readContentFromFile(String pathName) {
        Scanner fin;
        try {
            fin = new Scanner(new File(pathName));
        }
        catch(FileNotFoundException e) {
            return false;
        }

        String[] contentValue = new String[0];

        while(fin.hasNextLine()) {
            String line = fin.nextLine();
            contentValue = Arrays.copyOf(contentValue, contentValue.length + 1);
            contentValue[contentValue.length - 1] = line;
        }

        fin.close();
        this.content = contentValue;
        return true;
    }

    @Override
    public boolean writeContentToFile(String pathName) {
        try(FileWriter writer = new FileWriter(pathName)) {
            writer.write("==================== " + this.getTitle() + " ====================\n");
           for(String line : content)
               writer.write(line + "\n");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    public String[] getContent() {
        return this.content;
    }
}
