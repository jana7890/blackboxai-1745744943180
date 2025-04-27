package librarymanagementsystem;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int copiesAvailable;

    public Book(int bookId, String title, String author, String genre, String isbn, int copiesAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.copiesAvailable = copiesAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }
}
