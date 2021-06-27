import java.util.Scanner;

public class Menu {

    static Scanner scanner = new Scanner(System.in);
    static Service service = new Service();
    static boolean loop = true;


    public static void display() {
        while (loop) {
            System.out.println("--------MAIN-MENU--------");
            System.out.println("1. Sign in");
            System.out.println("2. Sign up");
            System.out.println("3. Exit");
            System.out.println("-------------------------");

            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    System.out.println("--------LOGIN-------");
                    service.login();

                    if (Service.currentProfile.isLoggedIn() && Service.currentProfile.getRole().equals("USER")) {
                        boolean userLoop = true;
                        while (userLoop) {
                            System.out.println("--------USER-MENU-------");
                            System.out.println("1. Take book");
                            System.out.println("2. Return book");
                            System.out.println("3. See all available books");
                            System.out.println("4. All taken books by you");
                            System.out.println("5. For exit");

                            switch (Integer.parseInt(scanner.nextLine())) {
                                case 1:
                                    service.takeBook();
                                    break;
                                case 2:
                                    service.returnBook();
                                    break;
                                case 3:
                                    service.allAvailable();
                                    break;
                                case 4:
                                    service.allTakenBooks();
                                    break;
                                case 5:
                                    userLoop = false;
                                    break;
                            }
                        }
                        break;
                    }

                    if (Service.currentProfile.isLoggedIn() && Service.currentProfile.getRole().equals("ADMIN")) {
                        boolean adminLoop = true;
                        while (adminLoop) {
                            System.out.println("--------ADMIN-MENU-------");
                            System.out.println("1. Create admin");
                            System.out.println("2. Create author");
                            System.out.println("3. Create book");
                            System.out.println("4. Delete user");
                            System.out.println("5. Delete author");
                            System.out.println("6. Delete book");
                            System.out.println("7. For exit");

                            switch (Integer.parseInt(scanner.nextLine())) {
                                case 1:
                                    service.createAdmin();
                                    break;
                                case 2:
                                    service.createAuthor();
                                    break;
                                case 3:
                                    service.createBook();
                                    break;
                                case 4:
                                    service.deleteUser();
                                    break;
                                case 5:
                                    service.deleteAuthor();
                                    break;
                                case 6:
                                    service.deleteBook();
                                    break;
                                case 7:
                                    adminLoop = false;
                                    break;
                            }
                        }
                        break;
                    }
                case 2:
                    System.out.println("--------SIGN-UP--------");
                    service.createUser();
                    break;
                case 3:
                    loop = false;
                    break;

            }
        }
    }


}

