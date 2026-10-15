package model;
import java.time.LocalDate;

public class BorrowRecord {
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate returnDate;

    public BorrowRecord(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.issueDate = LocalDate.now();
        this.returnDate = null;
    }

    
    public BorrowRecord(Book book,Member member, LocalDate issueDate, LocalDate returnDate){
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void returnBook() {
        returnDate = LocalDate.now();
    }
}