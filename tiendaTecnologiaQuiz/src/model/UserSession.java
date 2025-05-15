package model;

public class UserSession {
    private static UserSession instance;

    private String username;
    private String role;

    // Private constructor to prevent instantiation
    private UserSession(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Static method to initialize or get the instance
    public static UserSession getInstance(String username, String role) {
        if (instance == null) {
            instance = new UserSession(username, role);
        }
        return instance;
    }

    // Overload for just accessing the session
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("User session has not been initialized.");
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    // Method to destroy session (e.g., on logout)
    public void destroy() {
        instance = null;
    }
}