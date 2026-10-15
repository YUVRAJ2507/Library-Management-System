package DAO.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;

import DAO.BorrowRecordDAO;
import config.DatabaseConnection;
import model.Book;
import model.BorrowRecord;
import model.Member;

public class MySQLBorrowRecordDAO implements BorrowRecordDAO {

        @Override
        public void addBorrowRecord(BorrowRecord record) {

                String sql = "INSERT INTO borrow_records " + "(book_id, member_id, issue_date, return_date) "
                                + "VALUES (?, ?, ?, ?)";

                try (
                                Connection connection = DatabaseConnection.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setInt(1, record.getBook().getBookId());
                        statement.setInt(2, record.getMember().getMemberId());
                        statement.setDate(3, java.sql.Date.valueOf(record.getIssueDate()));

                        if (record.getReturnDate() == null) {
                                statement.setNull(4, java.sql.Types.DATE);

                        } else {
                                statement.setDate(4, java.sql.Date.valueOf(record.getReturnDate()));
                        }

                        statement.executeUpdate();

                        System.out.println("Borrow record added to database successfully.");

                } catch (Exception e) {
                        System.out.println("Failed to add borrow record.");
                        e.printStackTrace();
                }
        }

        @Override
        public ArrayList<BorrowRecord> getAllBorrowRecords(
                        ArrayList<Book> books,
                        ArrayList<Member> members) {

                ArrayList<BorrowRecord> records = new ArrayList<>();

                String sql = "SELECT * FROM borrow_records";

                try (
                                Connection connection = DatabaseConnection.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql);
                                ResultSet resultSet = statement.executeQuery()) {

                        while (resultSet.next()) {

                                int bookId = resultSet.getInt("book_id");
                                int memberId = resultSet.getInt("member_id");

                                Book book = null;
                                Member member = null;

                                for (Book currentBook : books) {
                                        if (currentBook.getBookId() == bookId) {
                                                book = currentBook;
                                                break;
                                        }
                                }

                                for (Member currentMember : members) {
                                        if (currentMember.getMemberId() == memberId) {
                                                member = currentMember;
                                                break;
                                        }
                                }

                                if (book != null && member != null) {
                                        LocalDate issueDate = resultSet.getDate("issue_date").toLocalDate();
                                        java.sql.Date returnDateSql = resultSet.getDate("return_date");
                                        LocalDate returnDate = null;

                                        if (returnDateSql != null) {
                                                returnDate = returnDateSql.toLocalDate();
                                        }

                                        BorrowRecord record = new BorrowRecord(book, member, issueDate, returnDate);
                                        records.add(record);
                                }
                        }

                } catch (Exception e) {

                        System.out.println("Failed to retrieve borrow records.");
                        e.printStackTrace();
                }

                return records;
        }

        @Override
        public void updateReturnDate(int bookId, int memberId) {

                String sql = "UPDATE borrow_records " +
                                "SET return_date = ? " +
                                "WHERE book_id = ? " +
                                "AND member_id = ? " +
                                "AND return_date IS NULL";

                try (
                                Connection connection = DatabaseConnection.getConnection();
                                PreparedStatement statement = connection.prepareStatement(sql)) {

                        statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                        statement.setInt(2, bookId);
                        statement.setInt(3, memberId);
                        statement.executeUpdate();
                        System.out.println("Return date updated in database.");

                } catch (Exception e) {
                        System.out.println("Failed to update return date.");
                        e.printStackTrace();
                }
        }
}