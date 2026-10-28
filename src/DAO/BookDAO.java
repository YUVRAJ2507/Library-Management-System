package dao;
import java.util.ArrayList;
import model.Book;

public interface BookDAO {
    void addBook(Book book);
    ArrayList<Book> getAllBooks();
    void updateAvailability(int bookId, boolean available);
}