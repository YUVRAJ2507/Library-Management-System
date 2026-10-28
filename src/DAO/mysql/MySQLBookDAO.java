package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import dao.BookDAO;
import model.Book;

public class MySQLBookDAO implements BookDAO {

    @Override
    public void addBook(Book book) throws SQLException {

        String sql = "INSERT INTO books " + "(book_id, title, author, available) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getBookId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setBoolean(4, book.isAvailable());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected != 1) {
                throw new SQLException("Book could not be added.");
            }
        }
    }

    @Override
    public List<Book> getAllBooks() throws SQLException {

        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, available FROM books";

        try (Connection connection = DatabaseConnection.getConnection();
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
        }
        return books;
    }

    @Override
    public void updateAvailability(int bookId, boolean available)
            throws SQLException {
        String sql = "UPDATE books SET available = ? WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, available);
            statement.setInt(2, bookId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected != 1) {
                throw new SQLException("Book availability could not be updated for ID: "+ bookId);
            }
        }
    }
}
