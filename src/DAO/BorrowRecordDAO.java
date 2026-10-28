package dao;

import java.util.ArrayList;
import model.Book;
import model.Member;
import model.BorrowRecord;

public interface BorrowRecordDAO {

 void addBorrowRecord(BorrowRecord record);
 ArrayList<BorrowRecord> getAllBorrowRecords(ArrayList<Book> books,ArrayList<Member> members);
 void updateReturnDate(int bookId, int memberId);
}