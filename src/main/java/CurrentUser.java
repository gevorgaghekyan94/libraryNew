public class CurrentUser {
    private boolean loggedIn;
    private int currentUserId;
    private String role;

    public CurrentUser(boolean loggedIn, int currentUserId, String role) {
        this.loggedIn = loggedIn;
        this.currentUserId = currentUserId;
        this.role = role;
    }

    public CurrentUser() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "loggedIn=" + loggedIn +
                ", currentUserId=" + currentUserId +
                ", role='" + role + '\'' +
                '}';
    }
}

