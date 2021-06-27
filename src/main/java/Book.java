public class Book {

    private String title;
    private String authorName;

    public Book() {
    }

    public Book(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                "authorName='" + authorName + '\'' +
                '}';
    }
}
