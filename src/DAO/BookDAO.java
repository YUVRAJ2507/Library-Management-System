package dao;

import java.util.List;
import model.Book;
public interface BookDAO {
    void addBook(Book book) throws Exception;
    List<Book> getAllBooks() throws Exception;
    void updateAvailability(int bookId, boolean available) throws Exception;
}
