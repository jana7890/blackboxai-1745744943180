package librarymanagementsystem;

public abstract class User {
    protected int userId;
    protected String name;
    protected String role;

    public User(int userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
