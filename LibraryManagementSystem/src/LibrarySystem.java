package librarymanagementsystem;

import java.util.*;
import java.time.LocalDate;

public class LibrarySystem {
    private Map<Integer, Book> books;
    private Map<Integer, User> users;
    private Map<Integer, Transaction> transactions;

    private int nextBookId;
    private int nextUserId;
    private int nextTransactionId;

    public LibrarySystem() {
        books = new HashMap<>();
        users = new HashMap<>();
        transactions = new HashMap<>();
        nextBookId = 1;
        nextUserId = 1;
        nextTransactionId = 1;
    }

    // Book Management
    public Book addBook(String title, String author, String genre, String isbn, int copies) {
        Book book = new Book(nextBookId++, title, author, genre, isbn, copies);
        books.put(book.getBookId(), book);
        return book;
    }

    public boolean editBook(int bookId, String title, String author, String genre, String isbn, int copies) {
        Book book = books.get(bookId);
        if (book == null) return false;
        if (title != null) book.setTitle(title);
        if (author != null) book.setAuthor(author);
        if (genre != null) book.setGenre(genre);
        if (isbn != null) book.setIsbn(isbn);
        book.setCopiesAvailable(copies);
        return true;
    }

    public boolean removeBook(int bookId) {
        return books.remove(bookId) != null;
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    public Book getBookById(int bookId) {
        return books.get(bookId);
    }

    // User Management
    public User addUser(String name, String role) {
        User user;
        switch (role) {
            case "Admin":
                user = new Admin(nextUserId++, name);
                break;
            case "Librarian":
                user = new Librarian(nextUserId++, name);
                break;
            case "Member":
                user = new Member(nextUserId++, name);
                break;
            default:
                return null;
        }
        users.put(user.getUserId(), user);
        return user;
    }

    public boolean removeUser(int userId) {
        return users.remove(userId) != null;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User getUserById(int userId) {
        return users.get(userId);
    }

    // Search and Filtering
    public List<Book> searchBooks(String title, String author, String genre) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) continue;
            if (author != null && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) continue;
            if (genre != null && !book.getGenre().toLowerCase().contains(genre.toLowerCase())) continue;
            results.add(book);
        }
        return results;
    }

    // Borrow and Return System
    public Transaction issueBook(int bookId, int memberId) {
        Book book = books.get(bookId);
        User user = users.get(memberId);
        if (book == null || user == null || !(user instanceof Member)) return null;
        if (book.getCopiesAvailable() < 1) return null;

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(14);

        Transaction transaction = new Transaction(nextTransactionId++, book, (Member) user, issueDate, dueDate);
        transactions.put(transaction.getTransactionId(), transaction);
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        return transaction;
    }

    public boolean returnBook(int transactionId) {
        Transaction transaction = transactions.get(transactionId);
        if (transaction == null || transaction.getReturnDate() != null) return false;
        transaction.setReturnDate(LocalDate.now());
        transaction.calculateFine();
        Book book = transaction.getBook();
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        return true;
    }

    // Transaction History
    public List<Transaction> getTransactionHistory(int memberId) {
        List<Transaction> history = new ArrayList<>();
        for (Transaction t : transactions.values()) {
            if (t.getMember().getUserId() == memberId) {
                history.add(t);
            }
        }
        return history;
    }

    public Collection<Transaction> getAllTransactions() {
        return transactions.values();
    }
}
