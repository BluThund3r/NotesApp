package application;

import java.util.Arrays;

public abstract class GenericFolder {
    private static int genId = 1;
    private String name;
    private int id;
    private GenericNote[] notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GenericFolder() {
    }

    public GenericFolder(String name) {
        this.name = name;
        this.id = genId++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GenericNote[] getNotes() {
        return notes;
    }

    public GenericNote[] getNotesByTitle(String title) {
        if (notes == null)
            return null;
        GenericNote[] temp = null;
        for (GenericNote note : notes)
            if (note.getTitle().equalsIgnoreCase(title)) {
                if (temp == null) {
                    temp = new GenericNote[1];
                    temp[0] = note;
                } else {
                    temp = Arrays.copyOf(temp, temp.length + 1);
                    temp[temp.length - 1] = note;
                }
            }

        return temp;
    }

    public GenericNote getNoteById(int id) {
        if (notes == null)
            return null;
        for (GenericNote n : notes)
            if (n.getId() == id)
                return n;
        return null;
    }

    public void clear() {
        this.notes = null;
    }

    public String getName() {
        return this.name;
    }
}
