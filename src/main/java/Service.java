import java.util.List;
import java.util.Scanner;

public class Service {

    private static Scanner scanner = new Scanner(System.in);
    private static SQLController dao = new SQLController();
    public static CurrentUser currentProfile;

    public void login() {
        System.out.println("Enter login:");
        String login = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        currentProfile = dao.login(login, password);
    }

    public void createUser() {
        Profile user = new Profile();
        System.out.println("Enter login:");
        user.setLogin(scanner.nextLine());
        System.out.println("Enter password:");
        user.setPasswordHash(scanner.nextLine());
        dao.creatingUser(user);
    }

    public void createAdmin() {
        Profile admin = new Profile();
        System.out.println("Enter login:");
        admin.setLogin(scanner.nextLine());
        System.out.println("Enter password:");
        admin.setPasswordHash(scanner.nextLine());
        dao.creatingAdmin(admin);
    }

    public void createAuthor() {
        Author author = new Author();
        System.out.println("Enter name:");
        author.setName(scanner.nextLine());
        dao.creatingAuthor(author);
    }

    public void createBook() {
        Book book = new Book();
        System.out.println("Enter title:");
        book.setTitle(scanner.nextLine());
        System.out.println("Enter author name:");
        book.setAuthorName(scanner.nextLine());
        dao.creatingBook(book);
    }

    public void deleteBook() {
        System.out.println("Enter book title:");
        dao.deleteBook(scanner.nextLine());
    }

    public void deleteAuthor() {
        System.out.println("Enter author name:");
        dao.deleteAuthor(scanner.nextLine());
    }

    public void deleteUser() {
        System.out.println("Enter user name:");
        dao.deleteUser(scanner.nextLine());
    }

    public void takeBook() {
        System.out.println("Enter book name:");
        Book book = dao.takeBook(scanner.nextLine(), currentProfile.getCurrentUserId());
        if (book.getTitle() != null) {
            System.out.println(book);
        }
    }

    public void returnBook() {
        System.out.println("Enter book name:");
        dao.returnBook(scanner.nextLine());
    }

    public void allAvailable() {
        List<Book> books = dao.allAvailableBooks();
        books.forEach(each -> System.out.println(each));
    }


    public void allTakenBooks() {
        List<Book> books = dao.allTakenBooksByCurrentUser(currentProfile.getCurrentUserId());
        books.forEach(each -> System.out.println(each));
    }
}
