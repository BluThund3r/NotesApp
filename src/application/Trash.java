package application;

public class Trash extends GenericFolder {

    public Trash() {
        super("Trash");
    }

    public boolean isEmpty() {
        return this.getItems() == null || this.getItems().length == 0;
    }

    public void emptyTrash() {
        if (isEmpty())
            return;
        clear();
    }
}
