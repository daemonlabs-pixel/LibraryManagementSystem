package models;

public abstract class Account {

    private final int id;
    private final String username;
    private final String password;

    public Account(int id, String username, String password) {

        if (id <= 0 || username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                "Invalid information regarding account creation."
            );
        }

        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return password != null && this.password.equals(password);
    }

    public abstract String getRole();
}