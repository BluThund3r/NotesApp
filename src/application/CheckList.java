package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class CheckList extends GenericNote {
    private CheckListElement[] content;

    public CheckList(String title, String[] rawContent, Folder initialFolder) {
        super(title, initialFolder);
        this.content = new CheckListElement[rawContent.length];
        for(int i = 0; i < rawContent.length; ++ i)
            this.content[i] = new CheckListElement(rawContent[i]);
    }

    public CheckList(String title, Folder initialFolder) {
        super(title, initialFolder);
    }

    public CheckListElement[] getContent() {
        return content;
    }

    @Override
    public void showContent() {
        System.out.println("'" + this.getTitle() + "' Content:");
        for(CheckListElement el : content)
            System.out.println(el.toString());
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

        CheckListElement[] contentValue = new CheckListElement[0];

        while(fin.hasNextLine()) {
            String line = fin.nextLine();
            contentValue = Arrays.copyOf(contentValue, contentValue.length + 1);
            contentValue[contentValue.length - 1] = new CheckListElement(line);
        }

        fin.close();
        return true;
    }

    @Override
    public boolean writeContentToFile(String pathName) {
        // not implemented yet
        return false;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

}
