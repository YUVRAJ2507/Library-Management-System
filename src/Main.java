import java.util.Scanner;

import dao.BookDAO;
import dao.BorrowRecordDAO;
import dao.MemberDAO;

import dao.mysql.MySQLBookDAO;
import dao.mysql.MySQLBorrowRecordDAO;
import dao.mysql.MySQLMemberDAO;

import model.Book;
import model.Member;

import service.Library;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        BookDAO bookDAO = new MySQLBookDAO();
        MemberDAO memberDAO = new MySQLMemberDAO();
        BorrowRecordDAO borrowRecordDAO = new MySQLBorrowRecordDAO();

        Library library;

        // Initialize the library and load data from the database.
        try {
            library = new Library(
                    bookDAO,
                    memberDAO,
                    borrowRecordDAO
            );

        } catch (IllegalStateException e) {
            System.out.println(
                    "Unable to start the Library Management System.");
            System.out.println(
                    "Please check your database connection and configuration.");
            System.out.println("Details: " + e.getMessage());
            return;
        }

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

            choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1: {
                    int bookId = readPositiveInt("Enter Book ID: ");
                    String title = readNonEmpty("Enter Book Title: ");
                    String author = readNonEmpty("Enter Author: ");

                    library.addBook(new Book(bookId, title, author));
                    break;
                }

                case 2:
                    library.displayBooks();
                    break;

                case 3: {
                    int memberId = readPositiveInt("Enter Member ID: ");
                    String name = readNonEmpty("Enter Member Name: ");
                    String phone = readPhone();

                    library.addMember(
                            new Member(memberId, name, phone));
                    break;
                }

                case 4:
                    library.displayMembers();
                    break;

                case 5: {
                    int bookId = readPositiveInt("Enter Book ID: ");
                    int memberId = readPositiveInt("Enter Member ID: ");

                    library.issueBook(bookId, memberId);
                    break;
                }

                case 6: {
                    int bookId = readPositiveInt("Enter Book ID: ");
                    int memberId = readPositiveInt("Enter Member ID: ");

                    library.returnBook(bookId, memberId);
                    break;
                }

                case 7:
                    library.displayRecords();
                    break;

                case 8:
                    System.out.println(
                            "\nThank you for using the Library Management System!");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please select an option from 1 to 8.");
            }

        } while (choice != 8);

        scanner.close();
    }

    // Reads an integer and handles non-numeric input.
    private static int readInt(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Please enter a whole number.");
            }
        }
    }

    // Accepts only positive IDs.
    private static int readPositiveInt(String prompt) {

        while (true) {
            int value = readInt(prompt);

            if (value > 0) {
                return value;
            }

            System.out.println("ID must be greater than zero.");
        }
    }

    // Rejects empty or whitespace-only input.
    private static String readNonEmpty(String prompt) {

        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Validates phone-number formatting and digit count.
    private static String readPhone() {

        while (true) {
            String phone = readNonEmpty("Enter Phone: ");

            if (!phone.matches("[+0-9()\\s-]+")) {
                System.out.println(
                        "Invalid phone format. Use digits and optional +, spaces, - or parentheses.");
                continue;
            }

            long digitCount = phone.chars().filter(Character::isDigit).count();

            if (digitCount >= 7 && digitCount <= 15) {
                return phone;
            }

            System.out.println("Phone number must contain between 7 and 15 digits.");
        }
    }
}
