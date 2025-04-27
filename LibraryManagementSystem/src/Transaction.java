package librarymanagementsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private int transactionId;
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Transaction(int transactionId, Book book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void calculateFine() {
        LocalDate effectiveReturnDate = (returnDate != null) ? returnDate : LocalDate.now();
        if (effectiveReturnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, effectiveReturnDate);
            fine = daysLate * 1.0; // 1 unit fine per day late
        } else {
            fine = 0.0;
        }
    }
}
