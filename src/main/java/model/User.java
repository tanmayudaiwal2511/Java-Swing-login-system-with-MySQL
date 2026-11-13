package model;

import java.sql.Timestamp;

public class User {
    private final int id;
    private final String username;
    private final String email;
    private final String role;
    private final Timestamp lastLogin;

    public User(int id, String username, String email, String role, Timestamp lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.lastLogin = lastLogin;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Timestamp getLastLogin() { return lastLogin; }
    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
}