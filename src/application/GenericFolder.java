package application;

import java.util.Arrays;

public abstract class GenericFolder {
    private static int genId = 1;
    private String name;
    private int id;
    private GenericNote[] items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    {
        this.items = new GenericNote[0];
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

    public GenericNote[] getItems() {
        return items;
    }

    public GenericNote[] getItemsByTitle(String title) {
        GenericNote[] temp = null;
        for (GenericNote note : items)
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

    public GenericNote getItemById(int id) {
        if (items == null)
            return null;
        for (GenericNote n : items)
            if (n.getId() == id)
                return n;
        return null;
    }

    public void clear() {
        this.items = new GenericNote[0];
    }

    public String getName() {
        return this.name;
    }
    public void addItem(GenericNote ob) {
        this.items = Arrays.copyOf(this.items, this.items.length + 1);
        this.items[this.items.length - 1] = ob;
    }

    public GenericNote getItemByPosition(int position) {
        try {
            return this.items[position];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Note getNoteByPosition(int position) {
        try {
            if(this.items[position] instanceof CheckList)
                return null;
            return (Note)this.items[position];
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public CheckList getCheckListByPosition(int position) {
        try {
            if(this.items[position] instanceof Note)
                return null;
            return (CheckList) this.items[position];
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void removeItemAtPosition(int position) {
        try {
            GenericNote test = items[position];
            for(int i = position; i < items.length - 1; ++ i)
                items[i] = items[i + 1];
            items = Arrays.copyOf(items, items.length - 1);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Number out of bounds!");
        }
    }

    public void showAllNotes() {
        if (this.items.length == 0) {
            System.out.println("No available Notes!");
            return;
        }
        boolean printed = false;
        for(int i = 0; i < items.length; ++ i) {
            if(items[i] instanceof Note) {
                if(!printed)
                    System.out.println("Folder Notes: ");
                printed = true;
                System.out.println((i + 1) + ". " + items[i].getTitle());
            }
        }
        if(!printed)
            System.out.println("No available Notes!");
    }

    public void showAllItems() {
        if (this.items.length == 0) {
            System.out.println("Empty Folder!");
            return;
        }
        System.out.println("Folder Content: ");
        for(int i = 0; i < items.length; ++ i)
            System.out.println((i + 1) + ". " + items[i].getTitle() + ((items[i] instanceof Note)? "    (Note)" : "    (Checklist)"));
    }

    public void showAllChecklists() {
        if (this.items.length == 0) {
            System.out.println("No available Checklists!");
            return;
        }
        boolean printed = false;
        for(int i = 0; i < items.length; ++ i) {
            if(items[i] instanceof CheckList) {
                if(!printed)
                    System.out.println("Folder Checklists: ");
                printed = true;
                System.out.println((i + 1) + ". " + items[i].getTitle());
            }
        }
        if(!printed)
            System.out.println("No available Checklists!");
    }

    public boolean anyCheckList() {
        for(GenericNote elem : items)
            if(elem instanceof CheckList)
                return true;
        return false;
    }

    public boolean anyNote() {
        for(GenericNote elem : items)
            if(elem instanceof Note)
                return true;
        return false;
    }
}
