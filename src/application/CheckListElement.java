package application;

public class CheckListElement {
    private boolean checked;
    private String lineContent;

    public CheckListElement(String lineContent) {
        this.checked = false;
        this.lineContent = lineContent;
    }

    @Override
    public String toString() {
        return (checked? "[x]  " : "[ ]  ") + lineContent;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
