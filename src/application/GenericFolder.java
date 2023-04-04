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

    {
        this.notes = new GenericNote[0];
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
        this.notes = new GenericNote[0];
    }

    public String getName() {
        return this.name;
    }
    public void addNote(GenericNote ob) {
        this.notes = Arrays.copyOf(this.notes, this.notes.length + 1);
        this.notes[this.notes.length - 1] = ob;
    }

    public GenericNote getNoteByPosition(int position) {
        try {
            return this.notes[position];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void removeNoteAtPosition(int position) {
        try {
            GenericNote test = notes[position];
            for(int i = position; i < notes.length - 1; ++ i)
                notes[i] = notes[i + 1];
            notes = Arrays.copyOf(notes, notes.length - 1);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Number out of bounds!");
        }
    }

    public void showAllNotes() {
        if (this.notes.length == 0) {
            System.out.println("Empty Folder!");
            return;
        }
        System.out.println("Folder Content: ");
        for(int i = 0; i < notes.length; ++ i)
            System.out.println((i + 1) + ". " + notes[i].getTitle());
    }
}
