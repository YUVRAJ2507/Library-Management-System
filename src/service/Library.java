package service;

import java.util.ArrayList;

import DAO.BookDAO;
import DAO.MemberDAO;
import DAO.BorrowRecordDAO;

import model.Book;
import model.Member;
import model.BorrowRecord;

public class Library {

    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private ArrayList<BorrowRecord> records;

    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private BorrowRecordDAO borrowRecordDAO;

    public Library(BookDAO bookDAO, MemberDAO memberDAO, BorrowRecordDAO borrowRecordDAO) {

        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.borrowRecordDAO = borrowRecordDAO;

        books = new ArrayList<>();
        members = new ArrayList<>();
        records = new ArrayList<>();

        books = bookDAO.getAllBooks();
        members = memberDAO.getAllMembers();
        records = borrowRecordDAO.getAllBorrowRecords(books, members);

        for (BorrowRecord record : records) {
            if (record.getReturnDate() == null) {
                record.getMember().borrowBook(record.getBook());
            }
        }
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);
        books.add(book);
    }

    public void displayBooks() {

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("\n-------------- BOOKS AVAILABILITY IN LIBRARY -----------------");
        for (Book book : books) {
            System.out.println("--------------------------------");
            System.out.println("Book ID   : " + book.getBookId());
            System.out.println("Title     : " + book.getTitle());
            System.out.println("Author    : " + book.getAuthor());
            System.out.println("Status    : " + (book.isAvailable() ? "Available" : "Issued"));
            System.out.println("--------------------------------");
        }
    }

    public Book findBook(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }

        return null;
    }

    public void addMember(Member member) {
        memberDAO.addMember(member);
        members.add(member);
    }

    public void displayMembers() {

        if (members.isEmpty()) {
            System.out.println("No members available.");
            return;
        }

        System.out.println("\n--------------- ALL MEMBERS OF LIBRARY ----------------");

        for (Member member : members) {
            System.out.println("--------------------------------");
            System.out.println("Member ID : " + member.getMemberId());
            System.out.println("Name      : " + member.getName());
            System.out.println("Phone     : " + member.getPhone());
            System.out.println("Books     : " + member.getBorrowedBookCount());
            System.out.println("--------------------------------");
        }
    }

    public Member findMember(int memberId) {

        for (Member member : members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }

        return null;
    }

    public void issueBook(int bookId, int memberId) {

        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book is already issued.");
            return;
        }

        book.issueBook();
        member.borrowBook(book);

        bookDAO.updateAvailability(bookId, false);

        BorrowRecord record = new BorrowRecord(book, member);
        records.add(record);

        borrowRecordDAO.addBorrowRecord(record);

        System.out.println("Book issued successfully.");
    }

    public void returnBook(int bookId, int memberId) {

        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (book.isAvailable()) {
            System.out.println("This book is not currently issued.");
            return;
        }

        book.returnBook();
        member.returnBook(book);

        bookDAO.updateAvailability(bookId, true);

        for (BorrowRecord record : records) {

            if (record.getBook() == book
                    && record.getMember() == member
                    && record.getReturnDate() == null) {

                record.returnBook();

                borrowRecordDAO.updateReturnDate(bookId, memberId);
                break;
            }
        }

        System.out.println("Book returned successfully.");
    }

    public void displayRecords() {
        if (records.isEmpty()) {
            System.out.println("No borrowing records.");
            return;
        }

        System.out.println("\n---------------- BORROW RECORDS -----------------");

        for (BorrowRecord record : records) {
            System.out.println("--------------------------------");
            System.out.println("Book   : " + record.getBook().getTitle());
            System.out.println("Member : " + record.getMember().getName());
            System.out.println("Issued : " + record.getIssueDate());

            if (record.getReturnDate() == null) {
                System.out.println("Returned : Not Returned");
            } else {

                System.out.println("Returned : " + record.getReturnDate());
            }
            System.out.println("--------------------------------");
        }
    }
}