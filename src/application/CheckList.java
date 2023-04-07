package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CheckList extends GenericNote {
    private CheckListElement[] content;

    {
        this.content = new CheckListElement[0];
    }

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
        ScreenManipulator.clearScreen();
        System.out.println("==================== " + this.getTitle() + " ====================");
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

        this.content = contentValue;
        fin.close();
        return true;
    }

    @Override
    public boolean writeContentToFile(String pathName) {
        try(FileWriter writer = new FileWriter(pathName)) {
            writer.write("==================== " + this.getTitle() + " ====================\n");
            for(CheckListElement el : content)
                writer.write(el.toString() + "\n");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    public void printElementsWithLineNumbers() {
        System.out.println("============================ Checklist '" + this.getTitle() + "' ============================");
        for(int i = 0; i < this.getContent().length; ++ i)
            System.out.println((i + 1) + "|\t" + this.getContent()[i].toString());
    }

    public void toggleElements() {
        while(true) {
            ScreenManipulator.clearScreen();
            this.printElementsWithLineNumbers();
            System.out.print("\nElement to toggle (write a negative number to exit): ");
            int positionToToggle = new Scanner(System.in).nextInt();
            if (positionToToggle < 0)
                return;
            if(positionToToggle <= this.getContent().length)
                this.getContent()[positionToToggle - 1].toggleChecked();
        }
    }

}
