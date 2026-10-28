package service;

import java.util.ArrayList;

import dao.BookDAO;
import dao.BorrowRecordDAO;
import dao.MemberDAO;
import model.Book;
import model.Member;
import model.BorrowRecord;

public class Library {

    private final ArrayList<Book> books;
    private final ArrayList<Member> members;
    private final ArrayList<BorrowRecord> records;

    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowRecordDAO borrowRecordDAO;

    public Library(
            BookDAO bookDAO,
            MemberDAO memberDAO,
            BorrowRecordDAO borrowRecordDAO) {

        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.borrowRecordDAO = borrowRecordDAO;

        try {
            books = new ArrayList<>(bookDAO.getAllBooks());
            members = new ArrayList<>(memberDAO.getAllMembers());
            records = new ArrayList<>(
                    borrowRecordDAO.getAllBorrowRecords(books, members));

            // Reconstruct each member's active borrowings.
            for (BorrowRecord record : records) {
                if (record.getReturnDate() == null) {
                    record.getMember().borrowBook(record.getBook());
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException( "Unable to initialize the library from the database.", e);
        }
    }

    public void addBook(Book book) {
        try {
            bookDAO.addBook(book);
            books.add(book);

        } catch (Exception e) {
            System.out.println("Unable to add book: " + e.getMessage());
        }
    }

    public void displayBooks() {

        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println(
                "\n-------------- BOOKS AVAILABILITY IN LIBRARY -----------------");
        for (Book book : books) {
            System.out.println("--------------------------------");
            System.out.println("Book ID   : " + book.getBookId());
            System.out.println("Title     : " + book.getTitle());
            System.out.println("Author    : " + book.getAuthor());
            System.out.println("Status    : "+ (book.isAvailable() ? "Available" : "Issued"));
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

        try {
            memberDAO.addMember(member);
            members.add(member);

        } catch (Exception e) {
            System.out.println("Unable to add member: " + e.getMessage());
        }
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

    private BorrowRecord findActiveBorrowRecord(int bookId, int memberId) {

        for (BorrowRecord record : records) {

            if (record.getBook().getBookId() == bookId
                    && record.getMember().getMemberId() == memberId
                    && record.getReturnDate() == null) {
                return record;
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

        BorrowRecord record = new BorrowRecord(book, member);

        try {
            bookDAO.updateAvailability(bookId, false);

            try {
                borrowRecordDAO.addBorrowRecord(record);
            } catch (Exception e) {
                // Compensate if inserting the borrowing record fails.
                try {
                    bookDAO.updateAvailability(bookId, true);
                } catch (Exception rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }

            // Update memory only after both database operations succeed.
            book.issueBook();
            member.borrowBook(book);
            records.add(record);

            System.out.println("Book issued successfully.");

        } catch (Exception e) {
            System.out.println("Unable to issue book: " + e.getMessage());
        }
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

        BorrowRecord activeRecord = findActiveBorrowRecord(bookId, memberId);

        if (activeRecord == null) {
            System.out.println("This member does not have an active record for this book.");
            return;
        }

        try {
            bookDAO.updateAvailability(bookId, true);

            try {
                borrowRecordDAO.updateReturnDate(bookId, memberId);
            } catch (Exception e) {
                // Compensate if updating the return date fails.
                try {
                    bookDAO.updateAvailability(bookId, false);
                } catch (Exception rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

            // Update memory only after both database operations succeed.
            activeRecord.returnBook();
            book.returnBook();
            member.returnBook(book);

            System.out.println("Book returned successfully.");

        } catch (Exception e) {
            System.out.println("Unable to return book: " + e.getMessage());
        }
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
