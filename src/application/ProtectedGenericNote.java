package application;

public abstract class ProtectedGenericNote extends GenericNote {
    String passwordHash;

    public String getPasswordHash() {
        return this.passwordHash;
    }
}
