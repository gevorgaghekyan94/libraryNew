import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        CreateDatabaseAndTables.createDatabaseAndTables();
        Menu.display();
    }
}
