import java.util.Scanner;

import DAO.BookDAO;
import DAO.MemberDAO;
import DAO.BorrowRecordDAO;

import DAO.mysql.MySQLBookDAO;
import DAO.mysql.MySQLMemberDAO;
import DAO.mysql.MySQLBorrowRecordDAO;

import model.Book;
import model.Member;

import service.Library;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookDAO bookDAO = new MySQLBookDAO();
        MemberDAO memberDAO = new MySQLMemberDAO();
        BorrowRecordDAO borrowRecordDAO = new MySQLBorrowRecordDAO();

        Library library = new Library(bookDAO, memberDAO, borrowRecordDAO);

        int choice;

        do {

            System.out.println();
            System.out.println("==================================");
            System.out.println("     LIBRARY MANAGEMENT SYSTEM");
            System.out.println("==================================");

            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Add Member");
            System.out.println("4. View Members");
            System.out.println("5. Issue Book");
            System.out.println("6. Return Book");
            System.out.println("7. View Borrow Records");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {

                case 1:

                    scanner.nextLine();

                    System.out.print("Enter Book ID: ");
                    int bookId = scanner.nextInt();

                    scanner.nextLine();

                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();

                    library.addBook(new Book(bookId, title, author));
                    break;

                case 2:

                    library.displayBooks();
                    break;

                case 3:

                    System.out.print("Enter Member ID: ");
                    int memberId = scanner.nextInt();

                    scanner.nextLine();

                    System.out.print("Enter Member Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();

                    library.addMember(new Member(memberId, name, phone));
                    break;

                case 4:

                    library.displayMembers();
                    break;

                case 5:

                    System.out.print("Enter Book ID: ");
                    int issueBookId = scanner.nextInt();

                    System.out.print("Enter Member ID: ");
                    int issueMemberId = scanner.nextInt();

                    library.issueBook(issueBookId, issueMemberId);
                    break;

                case 6:

                    System.out.print("Enter Book ID: ");
                    int returnBookId = scanner.nextInt();

                    System.out.print("Enter Member ID: ");
                    int returnMemberId = scanner.nextInt();

                    library.returnBook(returnBookId, returnMemberId);
                    break;

                case 7:

                    library.displayRecords();
                    break;

                case 8:

                    System.out.println("\nThank you for using the Library Management System!");
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 8);

        scanner.close();
    }
}