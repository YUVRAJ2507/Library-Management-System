package DAO.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DAO.BookDAO;
import config.DatabaseConnection;
import model.Book;

public class MySQLBookDAO implements BookDAO {

    @Override
    public void addBook(Book book) {

        String sql = "INSERT INTO books " + "(book_id, title, author, available) " + "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getBookId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setBoolean(4, book.isAvailable());
            statement.executeUpdate();
            System.out.println("Book added to database successfully.");

        } catch (Exception e) {
            System.out.println("Failed to add book.");
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Book> getAllBooks() {

        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean available = resultSet.getBoolean("available");
                Book book = new Book(bookId, title, author);

                if (!available) {
                    book.issueBook();
                }
                books.add(book);
            }

        } catch (Exception e) {

            System.out.println("Failed to retrieve books.");
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void updateAvailability(int bookId, boolean available) {
        String sql = "UPDATE books " + "SET available = ? " + "WHERE book_id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, bookId);
            statement.executeUpdate();

            System.out.println("Book availability updated in database.");

        } catch (Exception e) {
            System.out.println("Failed to update book availability.");
            e.printStackTrace();
        }
    }
}