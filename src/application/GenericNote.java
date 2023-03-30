package application;

import java.util.Date;

public abstract class GenericNote {
    private static int genId = 1;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String title;
    private final Date dateCreated;
    private Date dateModified;

    {
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.id = genId++;
    }

    public GenericNote() {
        this.title = "";
    }

    public GenericNote(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
}
