import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class CreateDatabaseAndTables {

    public static void createDatabaseAndTables() {
        createDatabase();
        createTableAuthors();
        createTableProfiles();
        createTableBooks();
    }

    public static void createDatabase() {

        try (Connection conn = DriverManager.getConnection(MariaDBConstants.URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                System.out.println("Connected to database library_new");

                String query = "CREATE DATABASE IF NOT EXISTS library_new";

                PreparedStatement preparedStatement = conn.prepareStatement(query);

                preparedStatement.executeUpdate();

            }
        } catch (SQLException ex) {
            System.out.println("Can`t create database:");
            ex.printStackTrace();
        }
    }

    public static void createTableProfiles() {

        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                System.out.println("Connected to table profiles");

                String query = "CREATE TABLE IF NOT EXISTS profiles (" +
                        "id int NOT NULL AUTO_INCREMENT," +
                        "login VARCHAR(255) UNIQUE ," +
                        "pass_hash VARCHAR(255)," +
                        "role ENUM('USER','ADMIN')," +
                        "PRIMARY KEY (id)" +
                        ");";

                PreparedStatement preparedStatement = conn.prepareStatement(query);

                preparedStatement.executeUpdate();

                String pass_hash = BCrypt.hashpw("admin",BCrypt.gensalt());
                String query1 = "INSERT INTO profiles(login,pass_hash,role) VALUES ('admin',?,'ADMIN')";

                PreparedStatement preparedStatement1 = conn.prepareStatement(query1);
                preparedStatement1.setString(1,pass_hash);

                preparedStatement1.executeUpdate();

            }
        } catch (SQLIntegrityConstraintViolationException ex){

        }
        catch (SQLException ex) {
            System.out.println("Cant create table profiles:");
            ex.printStackTrace();
        }
    }

    public static void createTableAuthors() {

        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                System.out.println("Connected to table authors");

                String query = "CREATE TABLE IF NOT EXISTS authors (" +
                        "id int NOT NULL AUTO_INCREMENT," +
                        "name VARCHAR(255) UNIQUE ," +
                        "PRIMARY KEY (id)" +
                        ");";

                PreparedStatement preparedStatement = conn.prepareStatement(query);

                preparedStatement.executeUpdate();

            }
        } catch (SQLException ex) {
            System.out.println("Cant create table authors:");
            ex.printStackTrace();
        }
    }

    public static void createTableBooks() {

        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                System.out.println("Connected to table books");

                String query = "CREATE TABLE IF NOT EXISTS books (" +
                        "id int NOT NULL AUTO_INCREMENT," +
                        "title VARCHAR(255) UNIQUE ," +
                        "author_id int NOT NULL ," +
                        "status ENUM('TAKEN','NOT_TAKEN') ," +
                        "profile_id int ," +
                        "return_date DATE," +
                        "PRIMARY KEY (id)," +
                        "FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE RESTRICT ," +
                        "FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE RESTRICT " +
                        ");";

                PreparedStatement preparedStatement = conn.prepareStatement(query);

                preparedStatement.executeUpdate();

            }
        } catch (SQLException ex) {
            System.out.println("Cant create table authors:");
            ex.printStackTrace();
        }
    }

}
