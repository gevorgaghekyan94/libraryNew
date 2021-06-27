import org.mindrot.jbcrypt.BCrypt;

public class Profile {

    private String login;
    private String passwordHash;
    private String salt = BCrypt.gensalt();

    public Profile(String login, String password) {
        this.login = login;
        this.passwordHash = BCrypt.hashpw(password,salt);
    }

    public Profile() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = BCrypt.hashpw(password,salt);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + login + '\'' +
                ", password='" + passwordHash + '\'' +
                '}';
    }
}
