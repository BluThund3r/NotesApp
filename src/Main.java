import java.util.Date;

class Menu {
    //TODO: Actiuni in meniu:
    // 1. Adauga User
    // 2. Login User
    // 3. Vizualizeaza o lista cu notitele user-ului
    // 4. Vizualizeaza continutul unei notite specificate de catre user
    // 5. Adauga o notita
    // 6. Sterge o notita
    // 7. Importa notita din fisier
    // 8. Exporta notita in fisier
    // 9. Logout User
    // 10. Creare folder
    // 11. Sterge folder
    // 12. Adauga notita in folder
    // 13. Tags
    // Maybe more to come

}

// Manages the authentication for user and protected notes that the user might access
class AuthService {

}

abstract class GenericFolder {
    private GenericNote[] notes;
}

class Trash extends GenericFolder{

}

class Folder extends GenericFolder{

}

abstract class GenericNote {
    private String title;
    private Date dateCreated;
    private Date dateModified;

    public GenericNote() {
        dateCreated = new Date();
        dateModified = new Date();
        this.title = "";
    }

    public GenericNote(String title) {
        dateCreated = new Date();
        dateModified = new Date();
        this.title = title;
    }


}

abstract class ProtectedGenericNote extends GenericNote{

}

class Note extends GenericNote{

}

class CheckList extends GenericNote {

}

class ProtectedNote extends ProtectedGenericNote {

}

class ProtectedCheckList extends ProtectedGenericNote {

}

class CheckListElement {

}

class User {
    private String userName;
    private String password;
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}