package application;

public interface ProtectedItem {
    public String getPasswordHash();

    public boolean itemAuthSuccessful();
}
