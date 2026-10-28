package dao;
import java.util.List;

import model.Book;
import model.Member;
import model.BorrowRecord;
public interface BorrowRecordDAO {

    void addBorrowRecord(BorrowRecord record) throws Exception;
    List<BorrowRecord> getAllBorrowRecords(
            List<Book> books,
            List<Member> members) throws Exception;
            
    void updateReturnDate(int bookId, int memberId) throws Exception;
}
