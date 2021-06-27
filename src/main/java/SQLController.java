import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLController {


    public CurrentUser login(String userName, String password) {

        CurrentUser currentUser = new CurrentUser();
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                String query = "SELECT id,pass_hash,role FROM profiles WHERE login =?";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, userName);

                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    if (BCrypt.checkpw(password, resultSet.getString("pass_hash"))) {
                        currentUser.setCurrentUserId(resultSet.getInt("id"));
                        currentUser.setRole(resultSet.getString("role"));
                        currentUser.setLoggedIn(true);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Cant sign in check login/password:");
            ex.printStackTrace();
        }
        return currentUser;
    }

    public void creatingUser(Profile profile) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                String query = "INSERT INTO profiles(login,pass_hash,role) VALUES (?,?,'USER')";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, profile.getLogin());
                preparedStatement.setString(2, profile.getPasswordHash());

                preparedStatement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException ex){
            System.out.println("This login already exists:");
        } catch (SQLException ex) {
            System.out.println("Cant create user:");
            ex.printStackTrace();
        }
    }

    public void creatingAdmin(Profile profile) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                String query = "INSERT INTO profiles(login,pass_hash,role) VALUES (?,?,'ADMIN')";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, profile.getLogin());
                preparedStatement.setString(2, profile.getPasswordHash());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Cant create admin:");
            ex.printStackTrace();
        }
    }

    public void creatingAuthor(Author author) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {
                String query = "INSERT INTO authors(name) VALUES (?)";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, author.getName());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Cant create author:");
            ex.printStackTrace();
        }
    }

    public void creatingBook(Book book) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "SELECT id FROM authors WHERE name = (?)";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, book.getAuthorName());

                ResultSet resultSet = preparedStatement.executeQuery();

                int authorId = 0;

                while (resultSet.next()) {
                    authorId = resultSet.getInt("id");
                }

                String query1 = "INSERT INTO books(title,author_id,status) VALUES (?,?,'NOT_TAKEN')";

                PreparedStatement preparedStatement1 = conn.prepareStatement(query1);
                preparedStatement1.setString(1, book.getTitle());
                preparedStatement1.setInt(2, authorId);

                preparedStatement1.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Cant create book:");
            ex.printStackTrace();
        }
    }

    public void deleteBook(String name) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "DELETE FROM books WHERE title = (?) AND status = 'NOT_TAKEN'";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, name);

                preparedStatement.executeUpdate();

            }
        } catch (SQLException ex) {
            System.out.println("Cant delete book check status or title!!!:");
            ex.printStackTrace();
        }
    }

    public void deleteAuthor(String name) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "DELETE FROM authors WHERE name = (?) ";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, name);

                preparedStatement.executeUpdate();

            }
        }catch (SQLIntegrityConstraintViolationException ex){
            System.out.println("Please delete that author`s Book before deleting author");
        }        catch (SQLException ex) {
            System.out.println("Cant delete author check name!!!:");
            ex.printStackTrace();
        }
    }

    public void deleteUser(String name) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "DELETE FROM profiles WHERE login = (?)";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, name);

                preparedStatement.executeUpdate();
            }
        }catch (SQLIntegrityConstraintViolationException ex){
            System.out.println("Please return books to library after removing profile");
        }
        catch (SQLException ex) {
            System.out.println("Cant delete user check user login!!!:");
            ex.printStackTrace();
        }
    }

    public Book takeBook(String title, int currentUserID) {
        Book book = new Book();
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "UPDATE books SET profile_id = ?,status = 'TAKEN', return_date = ? WHERE title = (?) AND status = 'NOT_TAKEN'";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, currentUserID);
                preparedStatement.setDate(2, Date.valueOf(LocalDate.now().plusDays(15)));
                preparedStatement.setString(3, title);

                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                    String query1 = "SELECT author_id FROM books WHERE title = (?)";

                    PreparedStatement preparedStatement1 = conn.prepareStatement(query1);
                    preparedStatement1.setString(1, title);

                    ResultSet resultSet = preparedStatement1.executeQuery();

                    int authorId = 0;

                    while (resultSet.next()) {
                        authorId = resultSet.getInt("author_id");
                    }

                    String query2 = "SELECT name FROM authors WHERE id = (?)";

                    PreparedStatement preparedStatement2 = conn.prepareStatement(query2);
                    preparedStatement2.setInt(1, authorId);

                    ResultSet resultSet1 = preparedStatement2.executeQuery();
                    String authorName = null;
                    while (resultSet1.next()) {
                        authorName = resultSet1.getString("name");
                    }
                    book.setTitle(title);
                    book.setAuthorName(authorName);
                }else {
                    System.out.println("Book is not available");
                }

            }
        } catch (SQLException ex) {
            System.out.println("Cant take book!!!:");
            ex.printStackTrace();
        }
        return book;
    }

    public void returnBook(String title) {
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "UPDATE books SET profile_id = null,status = 'NOT_TAKEN', return_date = null WHERE title = (?) AND status = 'TAKEN' AND profile_id = ?";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, title);
                preparedStatement.setInt(2, Service.currentProfile.getCurrentUserId());

                preparedStatement.executeUpdate();

            }
        } catch (SQLException ex) {
            System.out.println("Cant return book!!!:");
            ex.printStackTrace();
        }
    }

    public List<Book> allAvailableBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "SELECT books.title as title, authors.name as author_name" +
                        " FROM books,authors" +
                        " WHERE books.status ='NOT_TAKEN' and books.author_id = authors.id;";

                PreparedStatement preparedStatement = conn.prepareStatement(query);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Book book = new Book();
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthorName(resultSet.getString("author_name"));
                    books.add(book);
                }

            }
        } catch (SQLException ex) {
            System.out.println("Cant return not taken books:");
            ex.printStackTrace();
        }
        return books;
    }

    public List<Book> allTakenBooksByCurrentUser(int currentUserId) {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(MariaDBConstants.DB_URL, MariaDBConstants.USER, MariaDBConstants.PASS)) {
            if (conn != null) {

                String query = "SELECT books.title as title, authors.name as author_name" +
                        " FROM books,authors" +
                        " WHERE books.status = 'TAKEN' and books.profile_id = ? and books.author_id = authors.id;";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, currentUserId);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Book book = new Book();
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthorName(resultSet.getString("author_name"));
                    books.add(book);
                }

            }
        } catch (SQLException ex) {
            System.out.println("Cant return taken books:");
            ex.printStackTrace();
        }
        return books;
    }
}
